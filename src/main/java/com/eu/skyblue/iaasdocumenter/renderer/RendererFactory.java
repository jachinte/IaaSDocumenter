package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.uml.IaaSProfile;
import com.eu.skyblue.iaasdocumenter.utils.DisplayFormat;
import com.eu.skyblue.iaasdocumenter.utils.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 26/07/15
 * Time: 02:35
 * To change this template use File | Settings | File Templates.
 */
public class RendererFactory {
    private RendererFactory() {}

    public static GraphRenderer createRenderer(String displayFormat, Logger logger) throws Exception {
        if (displayFormat.equalsIgnoreCase(DisplayFormat.XMI)) {
            IaaSProfile iaaSProfile = new IaaSProfile(logger);
            return new XMIRenderer(iaaSProfile, logger);
        } else if (displayFormat.equalsIgnoreCase(DisplayFormat.PDF)) {
            return new PDFFormatRenderer(logger);
        } else if (displayFormat.equalsIgnoreCase(DisplayFormat.SVG)) {
            return new SVGFormatRenderer(logger);
        } else {
            throw new Exception("Unknown format");
        }
    }
}
