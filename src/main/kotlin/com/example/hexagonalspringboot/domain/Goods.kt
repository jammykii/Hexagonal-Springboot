package com.example.hexagonalspringboot.domain

import java.math.BigDecimal

data class Goods(
    val goodsId: Long,
    val goodsName: String,
    val brandNo: Long,
    val goodsImage: String,
    val goodsPrice: BigDecimal,
    val goodsOption1: String,
    val goodsOption2: String,
    val categoryId: Long,
) {

}