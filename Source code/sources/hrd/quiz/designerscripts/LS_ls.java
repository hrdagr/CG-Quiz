package hrd.quiz.designerscripts;

import anywheresoftware.b4a.keywords.LayoutBuilder;
import java.util.LinkedHashMap;

public class LS_ls {
    public static void LS_general(LinkedHashMap<String, LayoutBuilder.ViewWrapperAndAnchor> linkedHashMap, int i, int i2, float f) {
        LayoutBuilder.setScaleRate(0.3d);
        LayoutBuilder.scaleAll(linkedHashMap);
        linkedHashMap.get("imageview1").vw.setTop((int) (((1.0d * ((double) i2)) / 2.0d) - (((double) linkedHashMap.get("imageview1").vw.getHeight()) / 2.0d)));
    }
}
