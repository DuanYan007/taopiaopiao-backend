package com.duanyan.taopiaopiao.seckillservice.application.service;

import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatRequest;
import com.duanyan.taopiaopiao.seckillservice.api.dto.LockSeatResponse;

public interface SeckillService {

    LockSeatResponse lockSeats(LockSeatRequest request);
}
