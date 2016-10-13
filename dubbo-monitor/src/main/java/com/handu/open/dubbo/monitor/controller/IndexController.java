/**
 * Copyright 2006-2015 handu.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.handu.open.dubbo.monitor.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.handu.open.dubbo.monitor.DubboMonitorService;
import com.handu.open.dubbo.monitor.domain.DubboInvoke;
import com.handu.open.dubbo.monitor.domain.DubboInvokeLineChart;
import com.handu.open.dubbo.monitor.domain.LineChartSeries;
import com.handu.open.dubbo.monitor.support.CommonResponse;

/**
 * Home Controller
 *
 * @author Silence <me@chenzhiguo.cn>
 *         Created on 15/6/26.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private DubboMonitorService dubboMonitorService;

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "index";
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "loadTopData")
    public CommonResponse loadTopDate(String invokeDateFrom,String invokeDateTo,String type) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("EEE+MMM+dd+yyyy+HH:mm:ss+'GMT'Z",Locale.ENGLISH);
    	
    	DubboInvoke dubboInvoke = new DubboInvoke();
    	try {
			dubboInvoke.setInvokeDateFrom(sdf.parse(invokeDateFrom));
			dubboInvoke.setInvokeDateTo(sdf.parse(invokeDateTo));
		} catch (ParseException e) {
		}
    	dubboInvoke.setType(type);
    	
        CommonResponse commonResponse = CommonResponse.createCommonResponse();
        List<DubboInvokeLineChart> dubboInvokeLineChartList = new ArrayList<DubboInvokeLineChart>();
        DubboInvokeLineChart successDubboInvokeLineChart = new DubboInvokeLineChart();
        List<String> sxAxisCategories = Lists.newArrayList();
        LineChartSeries slineChartSeries = new LineChartSeries();
        List<double[]> sdataList = Lists.newArrayList();
        double[] data;
        Map dubboInvokeMap = dubboMonitorService.countDubboInvokeTopTen(dubboInvoke);
        List<DubboInvoke> success = (List<DubboInvoke>) dubboInvokeMap.get("success");
        for (DubboInvoke di : success) {
            sxAxisCategories.add(di.getMethod());
            data = new double[]{di.getSuccess()};
            sdataList.add(data);
        }
        slineChartSeries.setData(sdataList);
        slineChartSeries.setName("provider");

        successDubboInvokeLineChart.setxAxisCategories(sxAxisCategories);
        successDubboInvokeLineChart.setSeriesData(Arrays.asList(slineChartSeries));
        successDubboInvokeLineChart.setChartType("SUCCESS");
        successDubboInvokeLineChart.setTitle("The Top 20 of Invoke Success");
        successDubboInvokeLineChart.setyAxisTitle("t");
        dubboInvokeLineChartList.add(successDubboInvokeLineChart);

        DubboInvokeLineChart failureDubboInvokeLineChart = new DubboInvokeLineChart();
        List<String> fxAxisCategories = Lists.newArrayList();
        LineChartSeries flineChartSeries = new LineChartSeries();
        List<double[]> fdataList = Lists.newArrayList();
        List<DubboInvoke> failure = (List<DubboInvoke>) dubboInvokeMap.get("failure");
        for (DubboInvoke di : failure) {
            fxAxisCategories.add(di.getMethod());
            data = new double[]{di.getFailure()};
            fdataList.add(data);
        }
        flineChartSeries.setData(fdataList);
        flineChartSeries.setName("provider");

        failureDubboInvokeLineChart.setxAxisCategories(fxAxisCategories);
        failureDubboInvokeLineChart.setSeriesData(Arrays.asList(flineChartSeries));
        failureDubboInvokeLineChart.setChartType("FAILURE");
        failureDubboInvokeLineChart.setTitle("The Top 20 of Invoke Failure");
        failureDubboInvokeLineChart.setyAxisTitle("t");
        dubboInvokeLineChartList.add(failureDubboInvokeLineChart);

        commonResponse.setData(dubboInvokeLineChartList);
        return commonResponse;
    }
    
    public static void main(String args[]) throws ParseException
    {
    	 SimpleDateFormat sdf=new SimpleDateFormat("EEE+MMM+dd+yyyy+HH:mm:ss+'GMT'Z",Locale.ENGLISH);
    	 System.out.println(sdf.parse("Fri+Jul+01+2016+00:00:00+GMT+0800"));
    }
    
    
    
}
