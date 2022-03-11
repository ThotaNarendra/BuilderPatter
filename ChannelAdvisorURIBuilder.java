package test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChannelAdvisorURIBuilder {

    private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}:.]*[%\\p{Alnum}]*]|[^\\[/?#:]*)(:(\\{[^}]+\\}?|[^/?#]*))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");
    private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
    private String schema;
    private String userInfo;
    private String host;
    private int port;
    private String path;
    private String queryParam;
    private String fragment;

    private ChannelAdvisorURIBuilder() {
    }
    public static ChannelAdvisorURIBuilder builder(){
        return new ChannelAdvisorURIBuilder();
    }

    public ChannelAdvisorURIBuilder schema(String schema) {
        this.schema = schema;
        return this;
    }

    public ChannelAdvisorURIBuilder userInfo(String userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public ChannelAdvisorURIBuilder host(String host) {
        this.host = host;
        return this;
    }

    public ChannelAdvisorURIBuilder port(int port) {
        this.port = port;
        return this;
    }

    public ChannelAdvisorURIBuilder path(String path) {
        this.path = path;
        return this;
    }

    public ChannelAdvisorURIBuilder fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    public ChannelAdvisorURIBuilder queryParam(String name, String value){

        String formattedQuery = String.format("%s=%s",name,value);
        if(queryParam != null)
            this.queryParam += "&"+formattedQuery;
        else
            this.queryParam = formattedQuery;

        return this;
    }
    public static ChannelAdvisorURIBuilder fromUriString(String uri){

        Matcher matcher = URI_PATTERN.matcher(uri);
        if (matcher.matches()) {

            ChannelAdvisorURIBuilder channelAdvisorURIBuilder = new ChannelAdvisorURIBuilder();
            String schema = matcher.group(2);
            String userInfo = matcher.group(5);
            String host = matcher.group(6);
            String port = matcher.group(8);
            String path = matcher.group(9);
            String query = matcher.group(11);
            String fragment = matcher.group(13);

            channelAdvisorURIBuilder.schema = schema;
            channelAdvisorURIBuilder.userInfo = userInfo;
            channelAdvisorURIBuilder.host = host;
            channelAdvisorURIBuilder.port = Integer.parseInt(port);
            channelAdvisorURIBuilder.path = path;
            channelAdvisorURIBuilder.query(query);
            channelAdvisorURIBuilder.fragment = fragment;
            return channelAdvisorURIBuilder;
        }
        else {
            throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
        }

    }

    public ChannelAdvisorURIBuilder query( String query) {

        StringBuilder queryParams = new StringBuilder();
        if (query != null) {
            Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);

            while(matcher.find()) {
                String name = matcher.group(1);
                String eq = matcher.group(2);
                String value = matcher.group(3);
                String formattedQuery = String.format("%s%s%s",name,eq,value);
                queryParams.append(formattedQuery);
                if(matcher.end() !=0){
                    queryParams.append("&");
                }
            }
            this.queryParam = queryParams.toString();
        }
        return this;
    }
    public URI build(){

        try {
            return new URI(schema,userInfo,host,port,path,queryParam,fragment);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
