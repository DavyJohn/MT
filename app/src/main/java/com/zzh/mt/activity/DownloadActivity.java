package com.zzh.mt.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.download.DownloadInfo;
import com.zzh.mt.download.DownloadManager;
import com.zzh.mt.download.DownloadState;
import com.zzh.mt.download.DownloadViewHolder;
import com.zzh.mt.utils.SqliteTool;
import com.zzh.mt.widget.HorizontalProgressBarWithNumber;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;


public class DownloadActivity extends BaseActivity {

    @ViewInject(R.id.lv_download)
    private ListView downloadList;

    private DownloadManager downloadManager;
    private DownloadListAdapter downloadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.download_management));
        MyApplication.getInstance().add(this);
        downloadList = (ListView) findViewById(R.id.lv_download);
        downloadManager = DownloadManager.getInstance();
        downloadListAdapter = new DownloadListAdapter();
        downloadList.setAdapter(downloadListAdapter);

        downloadList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DownloadInfo downloadInfo = downloadManager.getDownloadInfo(position);
//                Log.e("下载地址：",downloadInfo.getFileSavePath());
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromFile(new File(downloadInfo.getFileSavePath()));
                if (downloadInfo.getType().substring(1,downloadInfo.getType().length()).equals("png") || downloadInfo.getType().substring(1,downloadInfo.getType().length()).equals("jpg")){
                    intent.setDataAndType(uri, "image/*");
                }else {
                    intent.setDataAndType(uri,"application/pdf");
                }
                startActivity(intent);


            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_download;
    }

    private class DownloadListAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater mInflater;

        private DownloadListAdapter() {
            mContext = getBaseContext();
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            if (downloadManager == null) return 0;
            return downloadManager.getDownloadListCount();
        }

        @Override
        public Object getItem(int i) {
            return downloadManager.getDownloadInfo(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DownloadItemViewHolder holder = null;
            DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
            if (view == null) {
                view = mInflater.inflate(R.layout.download_item, null);
                holder = new DownloadItemViewHolder(view, downloadInfo);
                view.setTag(holder);
                holder.refresh();
            } else {
                holder = (DownloadItemViewHolder) view.getTag();
                holder.update(downloadInfo);
            }

            if (downloadInfo.getState().value() < DownloadState.FINISHED.value()) {
                try {
                    downloadManager.startDownload(
                            downloadInfo.getUrl(),
                            downloadInfo.getLabel(),
                            String.valueOf(downloadInfo.getId()),
                            downloadInfo.getType(),
                            downloadInfo.getFileSavePath(),
                            downloadInfo.isAutoResume(),
                            downloadInfo.isAutoRename(),
                            holder);
                } catch (DbException ex) {
                    Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                }
            }

            return view;
        }
    }

    public class DownloadItemViewHolder extends DownloadViewHolder {
        @ViewInject(R.id.download_label)
        TextView label;
        @ViewInject(R.id.download_state)
        TextView state;
        @ViewInject(R.id.download_pb)
        ProgressBar progressBar;
        @ViewInject(R.id.download_stop_btn)
        Button stopBtn;
        @ViewInject(R.id.download_remove_btn)
        Button removeBtn;

        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
        }

        @Event(R.id.download_stop_btn)
        private void toggleEvent(View view) {
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    downloadManager.stopDownload(downloadInfo);
                    break;
                case ERROR:
                case STOPPED:
                    try {
                        downloadManager.startDownload(
                                downloadInfo.getUrl(),
                                downloadInfo.getLabel(),
                                String.valueOf(downloadInfo.getId()),
                                downloadInfo.getType(),
                                downloadInfo.getFileSavePath(),
                                downloadInfo.isAutoResume(),
                                downloadInfo.isAutoRename(),
                                this);
                    } catch (DbException ex) {
                        Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case FINISHED:
                    Toast.makeText(x.app(), "已经下载完成", Toast.LENGTH_LONG).show();
                    SqliteTool.getInstance().addData(mContext,downloadInfo.getUrlid());                    break;
                default:
                    break;
            }
        }

        @Event(R.id.download_remove_btn)
        private void removeEvent(View view) {
            try {
                downloadManager.removeDownload(downloadInfo);
                downloadListAdapter.notifyDataSetChanged();
                SqliteTool.getInstance().delete(mContext,downloadInfo.getUrlid());            } catch (DbException e) {
                Toast.makeText(x.app(), "移除任务失败", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void update(DownloadInfo downloadInfo) {
            super.update(downloadInfo);
            refresh();
        }

        @Override
        public void onWaiting() {
            refresh();
        }

        @Override
        public void onStarted() {
            refresh();
        }

        @Override
        public void onLoading(long total, long current) {
            refresh();
        }

        @Override
        public void onSuccess(File result) {
            refresh();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            refresh();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            refresh();
        }

        public void refresh() {
            label.setText(downloadInfo.getLabel());
//            state.setText(downloadInfo.getState().toString().equals("STOPPED")?getString(R.string.stop):"");
            progressBar.setProgress(downloadInfo.getProgress());
            stopBtn.setVisibility(View.VISIBLE);
            stopBtn.setText(getString(R.string.stop));

            // TODO: 2017/6/14  下载状态有问题基本就显示下载失败 和下载成功 和已下载
            DownloadState Dstate = downloadInfo.getState();
            switch (Dstate) {
                case WAITING:
                    state.setText(R.string.waiting);
                case STARTED:
                    state.setText(R.string.Downloading);
                    stopBtn.setText(getString(R.string.stop));
                    removeBtn.setVisibility(View.INVISIBLE);
                    break;
                case ERROR:
                    state.setText(R.string.Failed);
                case STOPPED:
                    stopBtn.setText(R.string.start);
                    break;
                case FINISHED:
                    state.setText(R.string.Finished);
                    stopBtn.setVisibility(View.INVISIBLE);
                    removeBtn.setVisibility(View.VISIBLE);
                    SqliteTool.getInstance().addData(mContext,downloadInfo.getUrlid());
                    break;
                default:
                    stopBtn.setText(R.string.start);
                    break;
            }
        }
    }
}
