package com.zijin.oep.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamInfoRequest {
    private ExamInfo examInfo;
    private List<QuestionInfo> questionInfos;
}
