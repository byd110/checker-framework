package javaexpression;

import org.checkerframework.framework.testchecker.javaexpression.qual.FlowExp;

import java.util.Collection;

public class UsePrivate {
    void test(Private app_ppts, Private test_ppts) {

        Collection<@FlowExp("app_ppts.nameToPpt") String> app_ppt_names = app_ppts.nameStringSet();
        Collection<@FlowExp("test_ppts.nameToPpt") String> test_ppt_names =
                test_ppts.nameStringSet();
    }
}
