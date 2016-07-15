package info.truemd.materialdesign.helper;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by yashvardhansrivastava on 02/04/16.
 */
public class CustomHtmlTextParsing {

    public static Spanned parse(String htmlText){

        String htmlS= htmlText;
        String htmlS1=htmlS.replace("<li>", "<br /> \u2022 ");
        String htmlS2=htmlS1.replace("</li>", " ");
        String htmlS3=htmlS2.replace("</h3>", "</b>");
        String htmlS4=htmlS3.replace("<h3>", "<br /><b>");
        String htmlS5=htmlS4.replace("\n","<br />");

        Spanned htmlAsSpanned = Html.fromHtml(htmlS5);

        return htmlAsSpanned;

    }
}
