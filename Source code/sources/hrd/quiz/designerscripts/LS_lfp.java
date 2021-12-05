package hrd.quiz.designerscripts;

import anywheresoftware.b4a.keywords.LayoutBuilder;
import java.util.LinkedHashMap;

public class LS_lfp {
    public static void LS_general(LinkedHashMap<String, LayoutBuilder.ViewWrapperAndAnchor> linkedHashMap, int i, int i2, float f) {
        LayoutBuilder.setScaleRate(0.3d);
        LayoutBuilder.scaleAll(linkedHashMap);
        linkedHashMap.get("lblscore").vw.setLeft((int) (((((double) i) * 1.0d) / 2.0d) - (((double) linkedHashMap.get("lblscore").vw.getWidth()) / 2.0d)));
        linkedHashMap.get("imageview1").vw.setLeft((int) (((((double) i) * 1.0d) / 2.0d) - (((double) linkedHashMap.get("imageview1").vw.getWidth()) / 2.0d)));
    }
}
