/*
 * Copyright (C) 2022 PixelExperience
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.flamingo.settings.lockscreen;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.text.TextUtils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.Utils;

import java.util.List;

public class UdfpsGesturePreferenceController extends BasePreferenceController {
    public static final String KEY = "screen_off_udfps_enabled";
    private FingerprintManager mFingerprintManager;
    private List<FingerprintSensorPropertiesInternal> mSensorProperties;

    public UdfpsGesturePreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
        mFingerprintManager = Utils.getFingerprintManagerOrNull(context);

        if (mFingerprintManager != null)
            mSensorProperties = mFingerprintManager.getSensorPropertiesInternal();

    }
    public UdfpsGesturePreferenceController(Context context) {
        this(context, KEY);
    }

    private boolean isUdfps() {
        for (FingerprintSensorPropertiesInternal prop : mSensorProperties) {
            if (prop.isAnyUdfpsType()) {
               return true;
            }
        }
        return false;
    }

    @Override
    public int getAvailabilityStatus() {
        if (!isUdfps()){
            return UNSUPPORTED_ON_DEVICE;
        }
        if (mFingerprintManager != null &&
            (!mFingerprintManager.isHardwareDetected() || !mFingerprintManager.hasEnrolledFingerprints())) {
            return UNSUPPORTED_ON_DEVICE;
        }
        if (TextUtils.isEmpty(mContext.getResources().getString(com.android.internal.R.string.config_dozeUdfpsLongPressSensorType))){
            return UNSUPPORTED_ON_DEVICE;
        }
        return AVAILABLE;
    }
}