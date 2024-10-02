package com.thc.fallspradv.controller;

import com.thc.fallspradv.dto.DefaultDto;
import com.thc.fallspradv.dto.TbpostDto;
import com.thc.fallspradv.security.PrincipalDetails;
import com.thc.fallspradv.service.TbpostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "1-1. 게시글 API 안내",
        description = "게시글 관련 기능 정의한 RestController.")
@RequestMapping("/api/tbpost")
@RestController
public class TbpostRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TbpostService tbpostService;
    public TbpostRestController(TbpostService tbpostService) {
        this.tbpostService = tbpostService;
    }

    /**/

    @Operation(summary = "게시글 생성",
            description = "게시글 생성 컨트롤러 <br />"
                    + "@param TbpostDto.CreateReqDto <br />"
                    + "@return HttpStatus.CREATED(201) ResponseEntity\\<TbpostDto.CreateResDto\\> <br />"
                    + "@exception 필수 파라미터 누락하였을 때 등 <br />"
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<TbpostDto.CreateResDto> create(@Valid @RequestBody TbpostDto.CreateReqDto param,
                                                         HttpServletRequest request,
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        //logger.info("reqTbuserId : " + request.getAttribute("reqTbuserId"));
        //logger.info("reqTbuserId : " + request.getAttribute("reqTbuserId"));
        String reqTbuserId = null;
        if(principalDetails != null){
            reqTbuserId = principalDetails.getTbuser().getId();
        }
        param.setTbuserId(reqTbuserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tbpostService.create(param));
    }

    @Operation(summary = "게시글 수정",
            description = "게시글 수정 컨트롤러 <br />"
                    + "@param TbpostDto.UpdateReqDto <br />"
                    + "@return HttpStatus.OK(200) ResponseEntity\\<TbpostDto.CreateResDto\\> <br />"
                    + "@exception 필수 파라미터 누락하였을 때 등 <br />"
    )
    @PutMapping("")
    public ResponseEntity<TbpostDto.CreateResDto> update(@Valid @RequestBody TbpostDto.UpdateReqDto param){
        return ResponseEntity.status(HttpStatus.OK).body(tbpostService.update(param));
    }

    @Operation(summary = "게시글 상세 조회",
            description = "게시글 상세 조회 컨트롤러 <br />"
                    + "@param TbpostDto.DetailReqDto <br />"
                    + "@return HttpStatus.OK(200) ResponseEntity\\<TbpostDto.DetailResDto\\> <br />"
                    + "@exception 필수 파라미터 누락하였을 때 등 <br />"
    )
    @GetMapping("")
    public ResponseEntity<TbpostDto.DetailResDto> detail(@Valid DefaultDto.DetailReqDto param, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(tbpostService.detail(param));
    }
    @Operation(summary = "게시글 목록 전체 조회",
            description = "게시글 목록 전체 조회 컨트롤러 <br />"
                    + "@param TbpostDto.ListReqDto <br />"
                    + "@return HttpStatus.OK(200) ResponseEntity\\<TbpostDto.DetailResDto\\> <br />"
                    + "@exception 필수 파라미터 누락하였을 때 등 <br />"
    )
    @GetMapping("/list")
    public ResponseEntity<List<TbpostDto.DetailResDto>> list(@Valid TbpostDto.ListReqDto param){
        return ResponseEntity.status(HttpStatus.OK).body(tbpostService.list(param));
    }

    @Operation(summary = "게시글 목록 페이지 조회",
            description = "게시글 목록 페이지 조회 컨트롤러 <br />"
                    + "@param TbpostDto.PagedListReqDto <br />"
                    + "@return HttpStatus.OK(200) ResponseEntity\\<TbpostDto.PagedListResDto\\> <br />"
                    + "@exception 필수 파라미터 누락하였을 때 등 <br />"
    )
    @GetMapping("/plist")
    public ResponseEntity<DefaultDto.PagedListResDto> plist(@Valid TbpostDto.PagedListReqDto param){
        return ResponseEntity.status(HttpStatus.OK).body(tbpostService.pagedList(param));
    }
    @Operation(summary = "게시글 목록 스크롤 조회",
            description = "게시글 목록 스크롤 조회 컨트롤러 <br />"
                    + "@param TbpostDto.MoreListReqDto <br />"
                    + "@return HttpStatus.OK(200) ResponseEntity\\<TbpostDto.DetailResDto\\> <br />"
                    + "@exception 필수 파라미터 누락하였을 때 등 <br />"
    )
    @GetMapping("/mlist")
    public ResponseEntity<List<TbpostDto.DetailResDto>> mlist(@Valid TbpostDto.ScrollListReqDto param
            , HttpServletRequest request, HttpServletResponse response
    ){
        logger.info("reqTbuserId : " + request.getAttribute("reqTbuserId"));
        param.setReqTbuserId(request.getAttribute("reqTbuserId") + "");
        return ResponseEntity.status(HttpStatus.OK).body(tbpostService.scrollList(param));
    }

}
