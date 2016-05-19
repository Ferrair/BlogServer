package wqh.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import wqh.aop.PostIntercept;
import wqh.aop.UserIntercept;
import wqh.config.Result;
import wqh.model.Work;
import wqh.service.ServiceAbs;
import wqh.service.WorkService;

import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class WorkController extends Controller {
    private WorkService mService = ServiceAbs.getInstance(WorkService.class, this);
    Result mResult = new Result();

    /**
     * Index of the blog,show all info of all the work(the number of work is a bit)
     */
    @SuppressWarnings("unused")
    public void index() {
        List<Work> workAbstracts = mService.queryAll();
        if (workAbstracts.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(workAbstracts);
        }
        renderJson(mResult);
    }

    @Before(PostIntercept.class)
    @ActionKey("/work/publish")
    public void publish() {
        String title = getPara("title");
        String description = getPara("description");
        String url = getPara("url");
        String logoUrl = getPara("logoUrl");
        if (title == null || description == null || url == null || logoUrl == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        mResult.success(mService.publish(title, description, url, logoUrl));
        renderJson(mResult);
    }


    @ActionKey("/work/queryById")
    public void queryById() {
        Integer id = getParaToInt("id");
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        Work aWork = mService.queryById(id);
        if (aWork == null) {
            mResult.fail(101);
        } else {
            mResult.success(aWork);
        }
        renderJson(mResult);
    }

    @ActionKey("/work/queryByTitle")
    public void queryByTitle() {
        String title = getPara("title");
        if (title == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        List<Work> workList = mService.queryByTitle(title);
        if (workList.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(workList);
        }
        renderJson(mResult);
    }

    /**
     * Download this apk MUST login first.
     * fileName:the file name of the Work,for example----"Chatting.apk"
     */
    @Before(UserIntercept.class)
    @ActionKey("/work/download")
    public void download() {
        String fileName = getPara("fileName");
        if (fileName == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        renderFile(fileName);
    }
}
