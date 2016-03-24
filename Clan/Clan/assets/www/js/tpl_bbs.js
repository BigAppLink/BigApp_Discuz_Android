 <script id="tpl_bbs" type="text/html">
        <section id="main">

            <section class="bbsBox">
                <div class="info">
                    <p class="photo"><img class="avatar" src={{avatar}} alt="" uid={{authorID}}></p>

                    <p class="name"><span>{{author}}</span><em class="tags tags-lz">楼主</em></p>

                    <p class="date">1楼&emsp;{{dateline}}</p>
                </div>
                <div class="cont">
                    <strong><h3>{{subject}}</h3></strong>

                    <div class="expCont">{{message}}</div>
                </div>
            </section>

            <section class="replyBox">
                {% if hasComments == 0 %}
                {% if isLouzhuPage == 0 %}
                <p class="noReply">暂无回复，赶快来抢沙发吧~</p>
                {% else %}
                <p class="noReply">楼主很懒，什么都没留下~</p>
                {% /if %}
                {% else %}
                <ul class="replyItems">
                    {% for model in commentsArr %}
                    <li class="clearFix">
                        <div class="info">
                            <p class="photo"><img class="avatar" src={{model.avatar}} alt=""
                                                  uid={{model.authorid}}></p>

                            <p class="name"><span>{{model.author}}</span> {% if model.authorid
                                equalsString louzhuID %} <em class="tags tags-lz">楼主</em></p>{% /if
                            %}
                            <p class="date">{{model.position}}楼&emsp;{{model.dateline}}</p>
                        </div>
                        <div class="cont">
                            <div class="expCont">{{model.postmessage}}</div>
                        </div>
                        <div class="comments"><a class="btnReply" href="javascript:void(0);"
                                                 author={{model.author}} pid={{model.pid}}
                                                 tid={{model.tid}} dateline={{model.dateline}}
                                                 content={{model.content}}
                                                 onClick="javascript:void(0);"
                                                 title=""><i class="icon icon-reply"></i></a></div>
                    </li>
                    {% /for %}
                </ul>
                {% /if %}
            </section>

        </section>
    </script>