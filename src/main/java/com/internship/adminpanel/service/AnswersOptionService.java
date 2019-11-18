package com.internship.adminpanel.service;

import com.internship.adminpanel.repository.AnswersOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnswersOptionService {
    private final AnswersOptionRepository answersOptionRepository;
}
