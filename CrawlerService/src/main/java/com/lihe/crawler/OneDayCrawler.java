/*
 * Copyright (c) 2016. lihe-fund All Rights Reserved.
 */

package com.lihe.crawler;

import com.lihe.entity.FundValueEntity;
import com.lihe.entity.Key;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 公募基金基本信息
 *
 * @author leo
 * @version V1.0.0
 * @package com.qed.crawler
 * @date 16/7/13
 */
@Slf4j
@Component
public class OneDayCrawler extends BaseCrawler {
    private static final String PREFIX_jbgk = "http://fund.eastmoney.com/f10/jbgk_";//概况
    private static final String PREFIX_jjfl = "http://fund.eastmoney.com/f10/jjfl_";//费率
    private static final String PREFIX_jjjl = "http://fund.eastmoney.com/f10/jjjl_";//经理
    private static final String PREFIX_zcpz = "http://fund.eastmoney.com/f10/zcpz_";//资产配置
    private static final String PREFIX_ccmx = "http://fund.eastmoney.com/f10/ccmx_";//基金持仓
    private static final String PREFIX_jjjz = "http://fund.eastmoney.com/f10/jjjz_";//净值
    private static final String PREFIX_jdzf = "http://fund.eastmoney.com/f10/jdzf_";//涨幅
    private static final String PREFIX_ = "http://fund.eastmoney.com/";//基金


    public OneDayCrawler() {
        super();
        prefixs = CollectionUtils.arrayToList(new String[] {PREFIX_});
    }

    @Override protected void parseData(Page page) {
        WebURL webURL = page.getWebURL();
        String url = webURL.getURL();
        log.info(url);
        String[] strings = webURL.getPath().split("_");
        if (strings.length < 2)
            return;
        String code = Util.splitNumbers(strings[1].split("\\.")[0]);

        if (url.startsWith(PREFIX_jjjl)) {//基金经理
            crawlerService.grabPublicFund(jjjl(page, code));
        } else if (url.startsWith(PREFIX_jbgk)) {//基金基本信息
            crawlerService.grabPublicFund(jjjx(page, code));
        } else if (url.startsWith(PREFIX_zcpz)) {//资产配置
            zp2jz(zcpz(page, code));
        } else if (url.startsWith(PREFIX_jjfl)) {//基金费率
            crawlerService.grabPublicFund(fl2jx(jjfl(page, code)));
        } else if (url.startsWith(PREFIX_ccmx)) {//基金持仓
            crawlerService.grabMainFund(jjcc(page, code));
        } else if (url.startsWith(PREFIX_jjjz)) {//历史净值
            lljz(code);
        } else if (url.startsWith(PREFIX_jdzf)) {//净值涨幅
            jzzf(page, code);
        }
    }


    private void jzzf(Page page, String code) {
        List<Map<String, String>> datas = new ArrayList<>();
        HashMap<String, String> beanInfos = new LinkedHashMap<>();
        datas.add(beanInfos);
//        beanInfos.put(Key.CODE, code);
        Document doc = Jsoup.parse(((HtmlParseData) page.getParseData()).getHtml());
        final String date =
            doc.getElementsByClass("tfoot").get(0).getElementsByClass("left").text();
        if (!StringUtils.isEmpty(date)) {
            //涨幅
            String html_jdzf = restTemplate.getForObject(
                "http://fund.eastmoney.com/f10/FundArchivesDatas.aspx?type=jdzf&code=" + code,
                String.class, Collections.emptyMap());

            Document doc_jdzf = Jsoup.parse(html_jdzf);
            Elements info_jdzf = doc_jdzf.getElementsByTag("ul");
            if (info_jdzf != null) {
                StringBuilder builder = new StringBuilder();
                if (!CollectionUtils.isEmpty(info_jdzf)) {
                    info_jdzf.stream().filter(e -> !e.getElementsByClass("tor").isEmpty())
                        .forEach(e -> builder.append(e.text().split(" ")[1]).append(" "));
                }
                beanInfos.put(Key.TITLE, doc_jdzf.getElementsByClass("title").text());
                beanInfos.put(Util.splitDate(date), builder.toString());
            }
        }
        crawlerService
            .grabNetFund(
                FundValueEntity.builder().code(code).title(Key.KEY_JZZF).datas(datas).build());
    }

    private void lljz(String code) {
        List<Map<String, String>> datas = new ArrayList<>();
        HashMap<String, String> beanInfos = new LinkedHashMap<>();
        datas.add(beanInfos);
        String html_lsjz = restTemplate.getForObject(
            "http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=" + code
                + "&page=1&per=1000&sdate=&edate=", String.class, Collections.emptyMap());

        Document doc_lsjz = Jsoup.parse(html_lsjz);
        Elements info_lsjz = doc_lsjz.getElementsByTag("tbody");
        if (info_lsjz != null) {
            Elements elements = info_lsjz.select("tr");
            if (!CollectionUtils.isEmpty(elements)) {
                elements.forEach(e -> {
                    Elements tds = e.getElementsByTag("td");
                    beanInfos.put(tds.get(0).text(), tds.text());
                });
            }
        }
        crawlerService
            .grabNetFund(
                FundValueEntity.builder().code(code).title(Key.KEY_LLJZ).datas(datas).build());
    }

    public void zp2jz(Map<String, List<String>> beanInfos) {
        String code = beanInfos.get(Key.CODE).get(0);
        List<String> listP = beanInfos.get(Key.KEY_ZP2JZ);
        if (!CollectionUtils.isEmpty(listP)) {
            List<Map<String, String>> datas = new ArrayList<>();
            for (String p : listP) {
                Map<String, String> hashTF = new HashMap<>();
                hashTF.put(Key.BODY, p);
                datas.add(hashTF);
            }
            crawlerService
                .grabAssets(
                    FundValueEntity.builder().code(code).title(Key.KEY_ZP2JZ).datas(datas).build());
        }
    }

    public HashMap<String, String> fl2jx(Map<String, List<String>> beanInfos) {
        HashMap<String, String> hashTF = new HashMap<>();
        hashTF.put(Key.CODE, beanInfos.get(Key.CODE).get(0));
        List<String> listT = beanInfos.get("交易确认日");
        if (!CollectionUtils.isEmpty(listT)) {
            String[] ts = listT.get(0).split(" ");
            if (ts != null && ts.length > 4) {
                hashTF.put(ts[0], ts[1]);
                hashTF.put(ts[2], ts[3]);
            }
        }

        List<String> listF = beanInfos.get("运作费用");
        if (!CollectionUtils.isEmpty(listF)) {
            String[] fs = listF.get(0).split(" ");
            if (fs != null && fs.length > 6) {
                hashTF.put(fs[4], fs[5]);
            }
        }

        List<String> listP = beanInfos.get("资产配置明细");
        if (!CollectionUtils.isEmpty(listP)) {
            String[] ps = listP.get(0).split(" ");
            if (ps != null && ps.length > 6) {
                hashTF.put(ps[4], ps[5]);
            }
        }
        return hashTF;
    }

    private HashMap<String, String> jjcc(Page page, String code) {
        HashMap<String, String> beanInfos = new LinkedHashMap<>();
        beanInfos.put(Key.CODE, code);
        //涨幅
        String html_jjcc = restTemplate.getForObject(
            "http://fund.eastmoney.com/f10/FundArchivesDatas.aspx?type=jjcc&topline=1000&code="
                + code, String.class, Collections.emptyMap());
        Document doc_jjcc = Jsoup.parse(html_jjcc);
        Elements tables = doc_jjcc.getElementsByTag("table");
        if (tables != null && tables.size() > 1) {
            Element table = tables.get(0);
            StringBuilder headerBuilder = new StringBuilder();
            Elements elements = table.select("thead").select("th");
            elements.stream()
                .forEach(th -> headerBuilder.append(th.text().replaceAll(" ", "")).append(" "));
            beanInfos.put(Key.HEAD, headerBuilder.toString().trim());
            Elements tbody = table.getElementsByTag("tbody").select("tr");
            StringBuilder bodyBuilder = new StringBuilder();
            for (Element e : tbody) {
                e.select("td").stream().forEach(td -> {
                    bodyBuilder.append(td.text()).append(" ");
                });
            }
            beanInfos.put(Key.BODY, bodyBuilder.toString().trim());
            beanInfos
                .put(Key.TIME, table.parent().getElementsByClass("right").select("font").text());
        }
        return beanInfos;

    }

    private Map<String, List<String>> zcpz(Page page, String code) {
        return jjfl(page, code);
    }


    private Map<String, List<String>> jjfl(Page page, String code) {
        Map<String, List<String>> beanInfos = new LinkedHashMap<>();
        Document doc = Jsoup.parse(((HtmlParseData) page.getParseData()).getHtml());
        Elements info = doc.getElementsByClass("boxitem");
        if (info != null) {
            beanInfos.put(Key.CODE, Collections.singletonList(code));
            try {
                for (Element i : info) {
                    String title = i.getElementsByClass("left").text();
                    if (StringUtils.isEmpty(title))
                        continue;
                    List<String> list = beanInfos.getOrDefault(title, new ArrayList<>());
                    if (!beanInfos.containsKey(title)) {
                        beanInfos.put(title, list);
                    }
                    beanInfos.put(title + "-" + Key.HEAD,
                        Collections.singletonList(i.select("thead").select("th").text()));
                    beanInfos.put(title, list);
                    list.addAll(
                        i.select("tbody").select("tr").stream()
                            .map(tr -> tr.select("td").text())
                            .collect(Collectors.toList()));
                }
            } catch (Exception e) {
                logger.warn(info.text(), e);
            }
        }
        return beanInfos;
    }

    private HashMap<String, String> jjjx(Page page, String code) {
        HashMap<String, String> beanInfos = new LinkedHashMap<>();
        Document doc = Jsoup.parse(((HtmlParseData) page.getParseData()).getHtml());
        Elements info = doc.getElementsByClass("info");
        if (info != null) {
            beanInfos.put(Key.CODE, code);
            Elements elements = info.select("tr");
            try {
                for (Element tr : elements) {
                    if (elements.size() > 2) {
                        for (int index = 0; index < 2; index++) {
                            String th = tr.select("th").get(index).text();
                            String td = tr.select("td").get(index).text();
                            beanInfos.put(th, td);
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn(elements.text(), e);
            }

        }
        Elements exInfos = doc.getElementsByClass("boxitem");
        if (exInfos != null) {
            for (Element e : exInfos) {
                Elements left = e.getElementsByClass("left");
                if (null == left)
                    continue;
                beanInfos.put(left.text(), e.getElementsByTag("p").text());
            }
        }
        return beanInfos;

    }

    private HashMap<String, String> jjjl(Page page, String code) {
        HashMap<String, String> beanInfos = new LinkedHashMap<>();
        beanInfos.put(Key.CODE, code);
        Document doc = Jsoup.parse(((HtmlParseData) page.getParseData()).getHtml());
        Elements jl_intro = doc.getElementsByClass("jl_intro");
        if (jl_intro != null) {
            beanInfos.put("name",
                jl_intro.get(0).getElementsByClass("text").select("p").get(0).text()
                    .substring(3));
            beanInfos.put("img", jl_intro.select("img").attr("src"));
            beanInfos.put("intro", jl_intro.text());
        }
        return beanInfos;
    }

}
