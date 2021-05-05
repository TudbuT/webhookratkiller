package tudbut

import tudbut.net.http.HTTPContentType
import tudbut.net.http.HTTPHeader
import tudbut.net.http.HTTPRequest
import tudbut.net.http.HTTPRequestType
import tudbut.parsing.JSON
import tudbut.parsing.TCN
import tudbut.parsing.TCNArray
import tudbut.tools.Lock

class WebHookRatKiller {

    static void main(String[] args) {
        if(args.length == 0) {
            println 'Provide a webhook pls owo *pounces on you*'
            return
        }
        String hook = args[0]

        TCN ratShat = new TCN()
        TCNArray embeds = new TCNArray()
        TCN embed = new TCN()
        TCNArray embedFields = new TCNArray()
        TCN field = new TCN()
        for (i in 0..<10) {
            embeds.add(embed)
        }
        embed.set("fields", embedFields)
        for (i in 0..<25) {
            embedFields.add(field)
        }
        ratShat.set("tts", true) // Yum
        ratShat.set("content", "@everyone your rat got shat on by TudbuT EZZZZ")
        ratShat.set("username", "TudbuT#2624")
        ratShat.set("embeds", embeds)
        embed.set("title", "TudbuT\non top")
        embed.set("description", "ez\n" * 3)
        field.set("name", "TudbuT\non top")
        field.set("value", "ez\n" * 3)

        URL parsed = new URL(hook)

        Lock lock = new Lock()
        long rateLimit
        HTTPRequest request = new HTTPRequest(
                HTTPRequestType.POST, "https://" + parsed.getHost(), 443, parsed.getPath(), HTTPContentType.JSON,
                JSON.write(ratShat),
                new HTTPHeader("User-Agent", "Java")
        )
        for (int i = 1 ; i <= 60 ; i++) {
            def s = request.send().parse().getBody()
            if (s != '') {
                Long l =JSON.read(s).getLong("retry_after")
                if(l != null) {
                    rateLimit = l
                }
                else {
                    println 'This webhook got killed already.'
                    return
                }
            } else
                rateLimit = 0
            lock.lock(rateLimit + 5000 as int)
            println "Sent $i"
        }

        lock.waitHere()
        def s = ""
        for (int i = 0; i < 40 && s != '{"message": "Unknown Webhook", "code": 10015}' ; i++) {
            request = new HTTPRequest(
                    HTTPRequestType.POST, "https://" + parsed.getHost(), 443, parsed.getPath(), HTTPContentType.JSON,
                    '{"content": "@everyone Say goodbye to your webhook (=\\nTudbuT#2624 on top!","username":"TudbuT#2624"}',
                    new HTTPHeader("User-Agent", "Java")
            )
            HTTPRequest kill = new HTTPRequest(
                    HTTPRequestType.DELETE, "https://" + parsed.getHost(), 443, parsed.getPath(), HTTPContentType.JSON,
                    "{}",
                    new HTTPHeader("User-Agent", "Java")
            )
            request.send()
            Thread.sleep(2000)
            s = kill.send().parse().getBody()
            println 'Kill request sent.'
        }

        println 'Killed.'
    }
}
