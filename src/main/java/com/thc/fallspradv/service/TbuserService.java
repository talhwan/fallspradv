package com.thc.fallspradv.service;


import com.thc.fallspradv.dto.DefaultDto;
import com.thc.fallspradv.dto.TbuserDto;

import java.util.List;

public interface TbuserService {

    TbuserDto.CreateResDto logout(DefaultDto.DetailReqDto param);

    /*
    TbuserDto.CreateResDto access(TbuserDto.AccessReqDto param);
    TbuserDto.CreateResDto confirm(TbuserDto.ConfirmReqDto param);
    TbuserDto.CreateResDto email(TbuserDto.UidReqDto param);
    TbuserDto.CreateResDto id(TbuserDto.UidReqDto param);
    */
    TbuserDto.CreateResDto login(TbuserDto.LoginReqDto param);
    TbuserDto.CreateResDto signup(TbuserDto.SignupReqDto param);
    /**/
    TbuserDto.CreateResDto create(TbuserDto.CreateReqDto param);
    TbuserDto.CreateResDto update(TbuserDto.UpdateReqDto param);
    TbuserDto.DetailResDto detail(DefaultDto.DetailServDto param);
    List<TbuserDto.DetailResDto> list(TbuserDto.ListReqDto param);
    DefaultDto.PagedListResDto pagedList(TbuserDto.PagedListReqDto param);
    List<TbuserDto.DetailResDto> scrollList(TbuserDto.ScrollListReqDto param);
}
