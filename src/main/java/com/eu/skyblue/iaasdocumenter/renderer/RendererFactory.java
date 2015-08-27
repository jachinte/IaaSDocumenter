package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.uml.IaaSProfile;
import com.eu.skyblue.iaasdocumenter.utils.Logger;

/**
 * Create renderers for the various file formats
 */
public class RendererFactory {
    private RendererFactory() {}

    /**
     * Constructs a new <code>GraphRenderer</code> object.
     *
     * @param displayFormat  The file format required [XMI|PDF|SVG]
     * @param logger         Logger
     */
    public static GraphRenderer createRenderer(String displayFormat, Logger logger) throws Exception {
        if (displayFormat.equalsIgnoreCase(DisplayFormat.XMI)) {
            IaaSProfile iaaSProfile = new IaaSProfile(logger);
            return new XMIRenderer(iaaSProfile, logger);
        } else if (displayFormat.equalsIgnoreCase(DisplayFormat.PDF)) {
            return new PDFFormatRenderer(logger);
        } else if (displayFormat.equalsIgnoreCase(DisplayFormat.SVG)) {
            return new SVGFormatRenderer(logger);
        } else if (displayFormat.equalsIgnoreCase(DisplayFormat.EPS)) {
            return new EPSFormatRenderer(logger);
        } else {
            throw new Exception("Unknown format");
        }
    }
}
