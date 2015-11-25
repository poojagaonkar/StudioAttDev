package Utility;

/**
 * Created by PoojaG on 25-11-2015.
 */
public class HtmHelper
{
    public static String BuildHtml(String bodyText)
    {
        String boilerplateBeforeBody = "<!DOCTYPE html> <html> <head><meta charset='utf-8'><title></title><meta name='description' content=''><meta name='HandheldFriendly' content='True'><meta name='MobileOptimized' content='480'><meta name='viewport' content='width=device-width, initial-scale=1, minimal-ui'> <meta http-equiv='cleartype' content='on'> </head> <body> <style>img{ width:100% !important; height:auto !important; margin: 5px 0px!important;} a {  word-wrap: break-word; } *{ word-wrap: break-word;} </style>";

        String boilerplateAfterBody = "</body></html>";

        return boilerplateBeforeBody + bodyText + boilerplateAfterBody;
    }


}
