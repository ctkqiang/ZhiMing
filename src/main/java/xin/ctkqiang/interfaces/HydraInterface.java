package xin.ctkqiang.interfaces;

public interface HydraInterface {
    public void onInit();

    public void onClose();

    public void bruteForceHttpPostForm(String url, int port, int threads );
}
