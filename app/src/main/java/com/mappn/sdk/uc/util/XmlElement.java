package com.mappn.sdk.uc.util;

import com.mappn.sdk.common.utils.BaseUtils;
import com.mokredit.payment.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class XmlElement {
    private static final String[] g;
    private static final int h;
    private String a;
    private Map b = new LinkedHashMap();
    private List c = new ArrayList(10);
    private Map d = new LinkedHashMap();
    private List e = new ArrayList(30);
    private List f = new ArrayList(100);

    static {
        String[] strArr = {"&", "&amp;", "\"", "&quot;", "'", "&apos;", "<", "&lt;", ">", "&gt;"};
        g = strArr;
        h = strArr.length % 2 == 0 ? g.length : g.length - 1;
    }

    public XmlElement(String str) {
        this.a = str;
    }

    private void a(Writer writer) {
        if (this.b != null) {
            for (Map.Entry entry : this.b.entrySet()) {
                writer.append(" ").append((CharSequence) entry.getKey()).append("=").append("\"");
                a(writer, (String) entry.getValue());
                writer.append("\"");
            }
        }
    }

    private static void a(Writer writer, XmlElement xmlElement) {
        if (xmlElement.f.size() == 0) {
            writer.append("<").append((CharSequence) xmlElement.a);
            xmlElement.a(writer);
            writer.append(" ").append("/").append(">");
            return;
        }
        writer.append("<").append((CharSequence) xmlElement.a);
        xmlElement.a(writer);
        writer.append(">");
        for (Object obj : xmlElement.f) {
            if (obj != null) {
                if (obj instanceof String) {
                    a(writer, (String) obj);
                } else {
                    a(writer, (XmlElement) obj);
                }
            }
        }
        writer.append("<").append("/").append((CharSequence) xmlElement.a).append(">");
    }

    private static void a(Writer writer, String str) {
        if (str != null) {
            for (int i = 0; i < h; i += 2) {
                str = str.replace(g[i], g[i + 1]);
            }
            writer.append((CharSequence) str);
        }
    }

    private void a(XmlPullParser xmlPullParser) {
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            setAttribute(xmlPullParser.getAttributeName(i), xmlPullParser.getAttributeValue(i));
        }
        int next = xmlPullParser.next();
        while (next != 1) {
            if (next == 2) {
                XmlElement xmlElement = new XmlElement(xmlPullParser.getName());
                addChild(xmlElement);
                xmlElement.a(xmlPullParser);
            }
            if (next == 3) {
                return;
            }
            if (next == 4) {
                addText(xmlPullParser.getText());
            }
            next = xmlPullParser.next();
        }
    }

    public static XmlElement parseXml(InputStream inputStream) {
        XmlElement xmlElement = null;
        if (inputStream != null) {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            XmlPullParser newPullParser = newInstance.newPullParser();
            newPullParser.setInput(inputStream, "UTF-8");
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType == 2) {
                    xmlElement = new XmlElement(newPullParser.getName());
                    xmlElement.a(newPullParser);
                } else if (eventType != 0) {
                    try {
                        BaseUtils.W("XmlElement", XmlPullParser.TYPES[eventType]);
                    } catch (Throwable th) {
                        BaseUtils.E("XmlElement", "Oh! My God!", th);
                    }
                }
            }
        }
        return xmlElement;
    }

    public XmlElement addAttributes(Map map) {
        if (map != null) {
            this.b.putAll(map);
        }
        return this;
    }

    public XmlElement addChild(XmlElement xmlElement) {
        if (xmlElement != null && xmlElement.a != null) {
            this.e.add(xmlElement);
            this.f.add(xmlElement);
            if (!this.d.containsKey(xmlElement.a)) {
                this.d.put(xmlElement.a, new ArrayList(10));
            }
            ((List) this.d.get(xmlElement.a)).add(xmlElement);
        }
        return this;
    }

    public XmlElement addChildren(List list) {
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                addChild((XmlElement) it.next());
            }
        }
        return this;
    }

    public XmlElement addText(String str) {
        this.c.add(str);
        this.f.add(str);
        return this;
    }

    public XmlElement clear() {
        this.b.clear();
        this.c.clear();
        this.d.clear();
        this.e.clear();
        this.f.clear();
        return this;
    }

    public List getAllChildren() {
        return this.e;
    }

    public List getAllText() {
        return this.c;
    }

    public List getAllTextAndChildren() {
        return this.f;
    }

    public String getAttribute(String str) {
        return (String) this.b.get(str);
    }

    public Map getAttributes() {
        return this.b;
    }

    public XmlElement getChild(int i) {
        if (this.e.size() > i) {
            return (XmlElement) this.e.get(i);
        }
        return null;
    }

    public XmlElement getChild(String str, int i) {
        List children = getChildren(str);
        if (children == null || children.size() <= i) {
            return null;
        }
        return (XmlElement) children.get(i);
    }

    public List getChildren(String str) {
        return (List) this.d.get(str);
    }

    public String getName() {
        return this.a;
    }

    public String getText() {
        return getText(0);
    }

    public String getText(int i) {
        return this.c.size() > i ? (String) this.c.get(i) : StringUtils.EMPTY;
    }

    public XmlElement setAttribute(String str, String str2) {
        this.b.put(str, str2);
        return this;
    }

    public final String toString() {
        StringWriter stringWriter = new StringWriter();
        try {
            writeAsXml(stringWriter);
        } catch (IOException e) {
            BaseUtils.E("XmlElement", "Oh! My God!", e);
        }
        return stringWriter.toString();
    }

    public void writeAsXml(Writer writer) {
        a(writer, this);
    }
}
