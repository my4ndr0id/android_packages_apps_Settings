/*
 * Copyright (C) 2012 The CyanogenMod Project
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
 * limitations under the License.
 */

package com.android.settings.cmstats;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.MSimTelephonyManager;

public class Utilities {
    public static final String CARRIER_UNKNOWN = "Unknown";
    public static final String CARRIER_ID_UNKNOWN = "0";
    public static final String COUNTRY_UNKNOWN = "Unknown";

    public static String getUniqueID(Context ctx) {
        return digest(ctx.getPackageName() + Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    public static String getCarrier(Context ctx, int subscription) {
        final String carrier;
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        MSimTelephonyManager mSimTm = (MSimTelephonyManager) ctx
                .getSystemService(Context.MSIM_TELEPHONY_SERVICE);

        if (mSimTm.isMultiSimEnabled()) {
            carrier = mSimTm.getNetworkOperatorName(subscription);
        } else {
            carrier = tm.getNetworkOperatorName();
        }

        if ("".equals(carrier)) {
            return CARRIER_UNKNOWN;
        }
        return carrier;
    }

    public static String getCarrierId(Context ctx, int subscription) {
        final String carrierId;
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        MSimTelephonyManager mSimTm = (MSimTelephonyManager) ctx
                .getSystemService(Context.MSIM_TELEPHONY_SERVICE);


        if (mSimTm.isMultiSimEnabled()) {
            carrierId = mSimTm.getNetworkOperator(subscription);
        } else {
            carrierId = tm.getNetworkOperator();
        }

        if ("".equals(carrierId)) {
            return CARRIER_ID_UNKNOWN;
        }
        return carrierId;
    }

    public static String getCountryCode(Context ctx, int subscription) {
        final String countryCode;
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        MSimTelephonyManager mSimTm = (MSimTelephonyManager) ctx
                .getSystemService(Context.MSIM_TELEPHONY_SERVICE);


        if (mSimTm.isMultiSimEnabled()) {
            countryCode = mSimTm.getNetworkCountryIso(subscription);
        } else {
            countryCode = tm.getNetworkCountryIso();
        }

        if (countryCode.equals("")) {
            return COUNTRY_UNKNOWN;
        }
        return countryCode;
    }

    public static String getDevice() {
        return SystemProperties.get("ro.cm.device");
    }

    public static String getModVersion() {
        return SystemProperties.get("ro.cm.version");
    }

    public static String digest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new BigInteger(1, md.digest(input.getBytes())).toString(16)
                    .toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }
}
