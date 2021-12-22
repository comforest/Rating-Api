package com.nomean.rating.api

class RequestInvalidException(msg: String) : Exception(msg) {
    constructor() : this("Request is invalid")
}