package com.thc.fallspradv.mapper;

import com.thc.fallspradv.dto.DefaultDto;
import com.thc.fallspradv.dto.TbpostDto;

import java.util.List;

public interface TbpostMapper {
    /**/
    TbpostDto.DetailResDto detail(DefaultDto.DetailReqDto param);
    List<TbpostDto.DetailResDto> list(TbpostDto.ListReqDto param);

    List<TbpostDto.DetailResDto> scrollList(TbpostDto.ScrollListReqDto param);
    List<TbpostDto.DetailResDto> pagedList(TbpostDto.PagedListReqDto param);
    int pagedListCount(TbpostDto.PagedListReqDto param);
}
