package com.vk.itmo.exceptions;

public sealed class AlreadyExistBoardException extends Exception permits
        IllegalAlreadyExistBoardCredentialsException,
        AlreadyExistBoardCantBeSettedException,
        AlreadyExistBoardAlreadyExistsException,
        BoardAlreadyExistsException,
        PermissionException,
        AlreadyNotFoundException,
        StatusIncorrectException,
        ReportStatusIncorrectException{}
