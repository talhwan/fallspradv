package com.thc.fallspradv.service.impl;

import com.thc.fallspradv.domain.Tbpost;
import com.thc.fallspradv.domain.Tbuser;
import com.thc.fallspradv.dto.DefaultDto;
import com.thc.fallspradv.dto.TbpostDto;
import com.thc.fallspradv.mapper.TbpostMapper;
import com.thc.fallspradv.repository.TbpostRepository;
import com.thc.fallspradv.repository.TbuserRepository;
import com.thc.fallspradv.service.TbpostService;
import com.thc.fallspradv.util.AES256Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TbpostServiceImpl implements TbpostService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TbpostRepository tbpostRepository;
    private final TbpostMapper tbpostMapper;
    private final TbuserRepository tbuserRepository;
    public TbpostServiceImpl(
            TbpostRepository tbpostRepository
            , TbpostMapper tbpostMapper
            , TbuserRepository tbuserRepository
    ) {
        this.tbpostRepository = tbpostRepository;
        this.tbpostMapper = tbpostMapper;
        this.tbuserRepository = tbuserRepository;
    }

    /**/

    @Override
    public TbpostDto.CreateResDto create(TbpostDto.CreateReqDto param) {
        //사용자 등록 완료!
        Tbpost tbpost = tbpostRepository.save(param.toEntity());
        return tbpost.toCreateResDto();
    }

    @Override
    public TbpostDto.CreateResDto update(TbpostDto.UpdateReqDto param){
        Tbpost tbpost = tbpostRepository.findById(param.getId()).orElseThrow(()->new RuntimeException("no data"));
        if(param.getTitle() != null){
            tbpost.setTitle(param.getTitle());
        }
        if(param.getContent() != null){
            tbpost.setContent(param.getContent());
        }
        if(param.getAuthor() != null){
            tbpost.setAuthor(param.getAuthor());
        }
        tbpostRepository.save(tbpost);
        return tbpost.toCreateResDto();
    }

    @Override
    public TbpostDto.DetailResDto detail(DefaultDto.DetailReqDto param){
        TbpostDto.DetailResDto selectResDto = tbpostMapper.detail(param);
        if(selectResDto == null){ throw new RuntimeException("no data"); }
        return selectResDto;
    }

    @Override
    public List<TbpostDto.DetailResDto> list(TbpostDto.ListReqDto param){
        return detailList(tbpostMapper.list(param));
    }
    @Override
    public DefaultDto.PagedListResDto pagedList(TbpostDto.PagedListReqDto param) {
        int[] res = param.init(tbpostMapper.pagedListCount(param));
        return param.afterBuild(res, detailList(tbpostMapper.pagedList(param)));
    }
    @Override
    public List<TbpostDto.DetailResDto> scrollList(TbpostDto.ScrollListReqDto param){
        param.init();
        //혹시 내 글만 보고자 할때는 이렇게 바꿔줍시다!
        if("my".equals(param.getTbuserId())){
            param.setTbuserId(param.getReqTbuserId());
        }
        //혹시 유저 id 몰라서 유저 username 넣는 애들도 있겠지?
        if(param.getTbuserId() != null && !"".equals(param.getTbuserId())){
            Tbuser tbuser = tbuserRepository.findByUsername(param.getTbuserId());
            if(tbuser != null){
                param.setTbuserId(tbuser.getId());
            }
        }

        return detailList(tbpostMapper.scrollList(param));
    }

    public List<TbpostDto.DetailResDto> detailList(List<TbpostDto.DetailResDto> list){
        List<TbpostDto.DetailResDto> newList = new ArrayList<>();
        for(TbpostDto.DetailResDto each : list){
            newList.add(detail(DefaultDto.DetailServDto.builder().id(each.getId()).build()));
        }
        return newList;
    }
}
