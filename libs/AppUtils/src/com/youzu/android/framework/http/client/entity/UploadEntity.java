/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.youzu.android.framework.http.client.entity;

import com.youzu.android.framework.http.callback.RequestCallBackHandler;

/**
 * Created with IntelliJ IDEA.
 * User: wyouflf
 * Date: 13-7-3
 * Time: 下午1:40
 */
public interface UploadEntity {
    void setCallBackHandler(RequestCallBackHandler callBackHandler);
}
