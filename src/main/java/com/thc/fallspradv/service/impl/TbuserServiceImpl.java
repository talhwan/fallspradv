package com.thc.fallspradv.service.impl;

import com.thc.fallspradv.domain.*;
import com.thc.fallspradv.dto.DefaultDto;
import com.thc.fallspradv.dto.TbuserDto;
import com.thc.fallspradv.mapper.TbuserMapper;
import com.thc.fallspradv.repository.*;
import com.thc.fallspradv.service.AuthService;
import com.thc.fallspradv.service.TbuserService;
import com.thc.fallspradv.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TbuserServiceImpl implements TbuserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TbuserRepository tbuserRepository;
    private final TbuserMapper tbuserMapper;
    private final RoleTypeRepository roleTypeRepository;
    private final TbuserRoleTypeRepository tbuserRoleTypeRepository;
    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ExternalProperties externalProperties;
    public TbuserServiceImpl(
            TbuserRepository tbuserRepository
            , TbuserMapper tbuserMapper
            , RoleTypeRepository roleTypeRepository
            , TbuserRoleTypeRepository tbuserRoleTypeRepository
            , AuthService authService
            , BCryptPasswordEncoder bCryptPasswordEncoder
            , ExternalProperties externalProperties
    ) {
        this.tbuserRepository = tbuserRepository;
        this.tbuserMapper = tbuserMapper;
        this.roleTypeRepository = roleTypeRepository;
        this.tbuserRoleTypeRepository = tbuserRoleTypeRepository;
        this.authService = authService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.externalProperties = externalProperties;
    }

    public String encryptPw(String pw) {
        String newPw = "";
        try{
            logger.info("try!!!");
            String secretKey = "1234567890123456";
            newPw = AES256Cipher.AES_Encode(secretKey, pw);
        } catch (Exception e){
            throw new RuntimeException("AES encrypt failed");
        }
        return newPw;
    }

    @Override
    public TbuserDto.CreateResDto access(String param) throws Exception {
        /*
        //리프레쉬 토큰으로 유효한지 확인
        TokenFactory tokenFactory = new TokenFactory();
        String accessToken = tokenFactory.issueAccessToken(param);
        System.out.println("accessToken : " + accessToken);
        */
        //

        param = param.replace(externalProperties.getTokenPrefix(), "");
        System.out.println("refreshToken ?!!! : " + param);

        String accessToken = authService.issueAccessToken(param);

        return TbuserDto.CreateResDto.builder().id(accessToken).build();
    }
    @Override
    public TbuserDto.CreateResDto logout(DefaultDto.DetailReqDto param){
        return TbuserDto.CreateResDto.builder().id("logout").build();
    }
    @Override
    public TbuserDto.CreateResDto login(TbuserDto.LoginReqDto param){
        //param.setPassword(encryptPw(param.getPassword()));

        //비번 암호화를 위한 코드
        //param.setPassword(bCryptPasswordEncoder.encode(param.getPassword()));

        Tbuser tbuser = tbuserRepository.findByUsernameAndPassword(param.getUsername(), param.getPassword());
        if(tbuser == null){ return TbuserDto.CreateResDto.builder().id("not matched").build(); }

        //로그인에 성공했을 경우!
        // 리프레쉬 토큰을 만들어서 리턴해준다!
        TokenFactory tokenFactory = new TokenFactory();
        String refreshToken = tokenFactory.issueRefreshToken(tbuser.getId());

        return TbuserDto.CreateResDto.builder().id(refreshToken).build();
    }

    @Override
    public TbuserDto.CreateResDto signup(TbuserDto.SignupReqDto param){
        TbuserDto.CreateReqDto newParam = TbuserDto.CreateReqDto.builder().username(param.getUsername()).password(param.getPassword()).build();
        return create(newParam);
    }

    /**/

    @Override
    public TbuserDto.CreateResDto create(TbuserDto.CreateReqDto param) {
        //비번 암호화를 위한 코드
        param.setPassword(bCryptPasswordEncoder.encode(param.getPassword()));

        //사용자 등록 완료!
        Tbuser tbuser = tbuserRepository.save(param.toEntity());

        //권한은 그냥 ROLE_USER 로 강제 저장함!(임시코드)
        //처음 가입하는 사람은 그냥 USER 권한 줄꺼야! (typeName은 ROLE_USER)
        RoleType roleType = roleTypeRepository.findByTypeName("ROLE_USER");
        if(roleType == null){
            roleType = new RoleType();
            roleType.setId("user");
            roleType.setTypeName("ROLE_USER");
            roleTypeRepository.save(roleType);
        }
        TbuserRoleType tbuserRoleType = TbuserRoleType.of(tbuser, roleType);
        tbuserRoleTypeRepository.save(tbuserRoleType);

        return tbuser.toCreateResDto();
    }

    @Override
    public TbuserDto.CreateResDto update(TbuserDto.UpdateReqDto param){
        Tbuser tbuser = tbuserRepository.findById(param.getId()).orElseThrow(()->new RuntimeException("no data"));
        if(param.getName() != null){
            tbuser.setName(param.getName());
        }
        if(param.getNick() != null){
            tbuser.setNick(param.getNick());
        }
        if(param.getPhone() != null){
            tbuser.setPhone(param.getPhone());
        }
        if(param.getGender() != null){
            tbuser.setGender(param.getGender());
        }
        if(param.getContent() != null){
            tbuser.setContent(param.getContent());
        }
        if(param.getImg() != null){
            tbuser.setImg(param.getImg());
        }
        tbuserRepository.save(tbuser);
        return tbuser.toCreateResDto();
    }

    @Override
    public TbuserDto.DetailResDto detail(DefaultDto.DetailReqDto param){
        TbuserDto.DetailResDto selectResDto = tbuserMapper.detail(param);
        if(selectResDto == null){ throw new RuntimeException("no data"); }
        return selectResDto;
    }

    @Override
    public List<TbuserDto.DetailResDto> list(TbuserDto.ListReqDto param){
        return detailList(tbuserMapper.list(param));
    }
    @Override
    public DefaultDto.PagedListResDto pagedList(TbuserDto.PagedListReqDto param) {
        /*int[] res = param.init(tbuserMapper.pagedListCount(param));
        return param.afterBuild(res, detailList(tbuserMapper.pagedList(param)));*/

        return null;
    }
    @Override
    public List<TbuserDto.DetailResDto> scrollList(TbuserDto.ScrollListReqDto param){
        param.init();
        return detailList(tbuserMapper.scrollList(param));
    }

    public List<TbuserDto.DetailResDto> detailList(List<TbuserDto.DetailResDto> list){
        List<TbuserDto.DetailResDto> newList = new ArrayList<>();
        for(TbuserDto.DetailResDto each : list){
            newList.add(detail(DefaultDto.DetailServDto.builder().id(each.getId()).build()));
        }
        return newList;
    }
}
