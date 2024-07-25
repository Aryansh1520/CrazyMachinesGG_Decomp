package com.mongodb.util;

import com.mokredit.payment.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class Args {
    final Map<String, String> _options = new HashMap();
    final List<String> _params = new ArrayList();

    public Args(String[] args) {
        for (String s : args) {
            if (s.startsWith("-")) {
                String s2 = s.substring(1);
                int idx = s2.indexOf("=");
                if (idx < 0) {
                    this._options.put(s2, StringUtils.EMPTY);
                } else {
                    this._options.put(s2.substring(0, idx), s2.substring(idx + 1));
                }
            } else {
                this._params.add(s);
            }
        }
    }

    public String getOption(String name) {
        return this._options.get(name);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String p : this._options.keySet()) {
            s.append('-').append(p);
            String v = this._options.get(p);
            if (v.length() != 0) {
                s.append('=');
                if (v.indexOf(" ") >= 0) {
                    s.append('\"').append(v).append('\"');
                } else {
                    s.append(v);
                }
            }
        }
        for (String p2 : this._params) {
            s.append(' ');
            if (p2.indexOf(" ") >= 0) {
                s.append('\"').append(p2).append('\"');
            } else {
                s.append(p2);
            }
        }
        return s.toString();
    }
}
