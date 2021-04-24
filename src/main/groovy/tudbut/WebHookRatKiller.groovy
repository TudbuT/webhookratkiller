package tudbut

import tudbut.net.http.HTTPContentType
import tudbut.net.http.HTTPHeader
import tudbut.net.http.HTTPRequest
import tudbut.net.http.HTTPRequestType

class WebHookRatKiller {

    static void main(String[] args) {
        if(args.length == 0) {
            println 'Provide a webhook pls'
            return
        }
        String hook = args[0]

        URL parsed = new URL(hook)

        HTTPRequest request = new HTTPRequest(
                HTTPRequestType.POST, "https://" + parsed.getHost(), 443, parsed.getPath(), HTTPContentType.JSON,
                "{\"content\": \"@everyone your rat got shat on by TudbuT EZZZZ\",\"username\":\"TudbuT#2624\"}",
                new HTTPHeader("User-Agent", "Java")
        )
        for (int i = 0 ; i < 40 ; i++) {
            println request.send().parse().getBody()
        }

        Thread.sleep(60000)
        def s = ""
        for (int i = 0 ; i < 40 && s != "{\"message\": \"Unknown Webhook\", \"code\": 10015}" ; i++) {
            request = new HTTPRequest(
                    HTTPRequestType.POST, "https://" + parsed.getHost(), 443, parsed.getPath(), HTTPContentType.JSON,
                    "{\"content\": \"@everyone Say goodbye to your webhook :)\",\"username\":\"TudbuT#2624\"}",
                    new HTTPHeader("User-Agent", "Java")
            )
            HTTPRequest kill = new HTTPRequest(
                    HTTPRequestType.DELETE, "https://" + parsed.getHost(), 443, parsed.getPath(), HTTPContentType.JSON,
                    "{}",
                    new HTTPHeader("User-Agent", "Java")
            )
            println request.send().parse().getBody()
            Thread.sleep(2000)
            println "Kill request: ${s = kill.send().parse().getBody()}"
        }

        println 'Killed.'
    }
}
