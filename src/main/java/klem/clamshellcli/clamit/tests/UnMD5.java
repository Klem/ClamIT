package klem.clamshellcli.clamit.tests;

import org.apache.http.client.methods.HttpPost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class UnMD5 {

    public static void main(String[] args) {

        Document doc;
        HttpPost request = null;


        try {

            doc = Jsoup.connect("http://www.stringfunction.com/md5-decrypter.html")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Host", "www.stringfunction.com")
                    .header("Origin", "http://www.stringfunction.com")
                    .header("Referer", "http://www.stringfunction.com/md5-decrypter.html")
                    .header("User-Agent", "Mozilla / 5.0 (Windows NT 6.1;WOW64)AppleWebKit / 537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .data("string_md5", "a9c4cef5735770e657b7c25b9dcb807b")
                    .data("submit", "Decrypt")
                    .post();

            Element input = doc.getElementById("textarea_md5_decrypter");
            String token = input.val();
            System.out.println("Element found" + input);
            System.out.println("Extracted value" + token);
        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

}