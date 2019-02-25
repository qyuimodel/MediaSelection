package com.luck.picture.lib.tools;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 包名：com.luck.picture.lib.tools
 * 创建人：秦洋
 * 创建时间：2019/1/11
 * 结果回掉
 */
public interface CallBackLisener {
   void onResult(List<LocalMedia> images);
}
