<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="html"/>
    
<!--     <xsl:variable name="owner" select="'mohamed'"/>-->
    <xsl:variable name="owner" select="/history/@owner"/>
    

    <xsl:template match="/*[1]">
        <html>

            <head>
               
                <title><xsl:value-of select="$owner"/></title>
                <title>$owner</title>
                <style type="text/css">
                    @import url(http://fonts.googleapis.com/css?family=Lato:100,300,400,700);
                    @import url(http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css);
                    html,
                    body {
                    background: #e5e5e5;
                    font-family: 'Lato', sans-serif;
                    margin: 0px auto;
                    }

                    ::selection {
                    background: rgba(82, 179, 217, 0.3);
                    color: inherit;
                    }

                    a {
                    color: rgba(82, 179, 217, 0.9);
                    }
                    /* M E N U */

                    .menu {
                    position: fixed;
                    top: 0px;
                    left: 0px;
                    right: 0px;
                    width: 100%;
                    height: 50px;
                    background: rgba(82, 179, 217, 0.9);
                    z-index: 100;
                    -webkit-touch-callout: none;
                    -webkit-user-select: none;
                    -moz-user-select: none;
                    -ms-user-select: none;
                    }

                    .back {
                    position: absolute;
                    width: 90px;
                    height: 50px;
                    top: 0px;
                    left: 0px;
                    color: #fff;
                    line-height: 50px;
                    font-size: 30px;
                    padding-left: 10px;
                    cursor: pointer;
                    }

                    .back img {
                    position: absolute;
                    top: 5px;
                    left: 30px;
                    width: 40px;
                    height: 40px;
                    background-color: rgba(255, 255, 255, 0.98);
                    border-radius: 100%;
                    -webkit-border-radius: 100%;
                    -moz-border-radius: 100%;
                    -ms-border-radius: 100%;
                    margin-left: 15px;
                    }

                    .back:active {
                    background: rgba(255, 255, 255, 0.2);
                    }

                    .logo {
                    text-align: center;
                    font-size: 25px;
                    padding: 7px;
                    color: white;
                    }

                    .name {
                    position: absolute;
                    top: 3px;
                    left: 110px;
                    font-family: 'Lato';
                    font-size: 25px;
                    font-weight: 300;
                    color: rgba(255, 255, 255, 0.98);
                    cursor: default;
                    }

                   
                    /* M E S S A G E S */

                    .chat {
                    list-style: none;
                    background: none;
                    margin: 0;
                    padding: 0 0 50px 0;
                    margin-top: 60px;
                    margin-bottom: 10px;
                    }

                    .chat li {
                    padding: 0.5rem;
                    overflow: hidden;
                    display: flex;
                    }

                    .chat .avatar {
                    width: 40px;
                    height: 40px;
                    position: relative;
                    display: block;
                    z-index: 2;
                    border-radius: 100%;
                    -webkit-border-radius: 100%;
                    -moz-border-radius: 100%;
                    -ms-border-radius: 100%;
                    background-color: rgba(255, 255, 255, 0.9);
                    }

                    .chat .avatar img {
                    width: 40px;
                    height: 40px;
                    border-radius: 100%;
                    -webkit-border-radius: 100%;
                    -moz-border-radius: 100%;
                    -ms-border-radius: 100%;
                    background-color: rgba(255, 255, 255, 0.9);
                    -webkit-touch-callout: none;
                    -webkit-user-select: none;
                    -moz-user-select: none;
                    -ms-user-select: none;
                    }


                    .other .msg {
                    order: 1;
                    border-top-left-radius: 0px;
                    box-shadow: -1px 2px 0px #D4D4D4;
                    }

                    .other:before {
                    content: "";
                    position: relative;
                    top: 0px;
                    right: 0px;
                    left: 40px;
                    width: 0px;
                    height: 0px;
                    border: 5px solid #fff;
                    border-left-color: transparent;
                    border-bottom-color: transparent;
                    }

                    .self {
                    justify-content: flex-end;
                    align-items: flex-end;
                    }

                    .self .msg {
                    order: 1;
                    border-bottom-right-radius: 0px;
                    box-shadow: 1px 2px 0px #D4D4D4;
                    }

                    .self .avatar {
                    order: 2;
                    }

                    .self .avatar:after {
                    content: "";
                    position: relative;
                    display: inline-block;
                    bottom: 19px;
                    right: 0px;
                    width: 0px;
                    height: 0px;
                    border: 5px solid #fff;
                    border-right-color: transparent;
                    border-top-color: transparent;
                    box-shadow: 0px 2px 0px #D4D4D4;
                    }

                    .msg {
                    background: white;
                    min-width: 50px;
                    padding: 10px;
                    border-radius: 2px;
                    box-shadow: 0px 2px 0px rgba(0, 0, 0, 0.07);
                    }

                    .msg p {
                    font-size: 0.8rem;
                    margin: 0 0 0.2rem 0;
                    color: #777;
                    }

                    .msg img {
                    position: relative;
                    display: block;
                    width: 450px;
                    border-radius: 5px;
                    box-shadow: 0px 0px 3px #eee;
                    transition: all .4s cubic-bezier(0.565, -0.260, 0.255, 1.410);
                    cursor: default;
                    -webkit-touch-callout: none;
                    -webkit-user-select: none;
                    -moz-user-select: none;
                    -ms-user-select: none;
                    }

                    @media screen and (max-width: 800px) {
                    .msg img {
                    width: 300px;
                    }
                    }

                    @media screen and (max-width: 550px) {
                    .msg img {
                    width: 200px;
                    }
                    }

                    .msg time {
                    font-size: 0.7rem;
                    color: #ccc;
                    margin-top: 3px;
                    float: right;
                    cursor: default;
                    -webkit-touch-callout: none;
                    -webkit-user-select: none;
                    -moz-user-select: none;
                    -ms-user-select: none;
                    }

                    .msg time:before {
                    content: "\f017";
                    color: #ddd;
                    font-family: FontAwesome;
                    display: inline-block;
                    margin-right: 4px;
                    }

                    @-webikt-keyframes pulse {
                    from {
                    opacity: 0;
                    }
                    to {
                    opacity: 0.5;
                    }
                    }

                    ::-webkit-scrollbar {
                    min-width: 12px;
                    width: 12px;
                    max-width: 12px;
                    min-height: 12px;
                    height: 12px;
                    max-height: 12px;
                    background: #e5e5e5;
                    box-shadow: inset 0px 50px 0px rgba(82, 179, 217, 0.9), inset 0px -52px 0px #fafafa;
                    }

                    ::-webkit-scrollbar-thumb {
                    background: #bbb;
                    border: none;
                    border-radius: 100px;
                    border: solid 3px #e5e5e5;
                    box-shadow: inset 0px 0px 3px #999;
                    }

                    ::-webkit-scrollbar-thumb:hover {
                    background: #b0b0b0;
                    box-shadow: inset 0px 0px 3px #888;
                    }

                    ::-webkit-scrollbar-thumb:active {
                    background: #aaa;
                    box-shadow: inset 0px 0px 3px #7f7f7f;
                    }

                    ::-webkit-scrollbar-button {
                    display: block;
                    height: 26px;
                    }
                </style>
            </head>

            <body>
                <div class="menu">
                    <div class="back">
                        <img src="http://i.imgur.com/DY6gND0.png" draggable="false" />
                    </div>
                    <div class="name"><xsl:value-of select="message/to"/></div>
                    <div class="logo">Void Chat</div>
                </div>
                <ol class="chat">
                    
                    <xsl:for-each select="message">
                        <xsl:choose>
                            <xsl:when test="from = $owner">
                                <li class="self">
                                    <div class="avatar">
                                        <img src="http://i.imgur.com/HYcn9xO.png" draggable="false" />
                                    </div>
                                    <div class="msg">
                                        <p style="
                                    color:{color};
                                    font-size: {size}px;
                                    font-family: {family};
                                    text-decoration: {decoration};
                                    font-weight: {weight};
                                    font-style: {style};">
                                            <xsl:value-of select="body"/>
                                        </p>
                                        <time>
                                            <xsl:value-of select="date"/>
                                        </time>
                                    </div>
                                </li>
                            </xsl:when>
                            <xsl:otherwise>
                                <li class="other">
                                    <div class="avatar">
                                        <img src="http://i.imgur.com/DY6gND0.png" draggable="false" />
                                    </div>
                                    <div class="msg">
                                        <p style="
                                    color:{color};
                                    font-size: {size}px;
                                    font-family: {family};
                                    text-decoration: {decoration};
                                    font-weight: {weight};
                                    font-style: {style};">
                                            <xsl:value-of select="body"/>
                                        </p>
                                        <time>
                                            <xsl:value-of select="date"/>
                                        </time>
                                    </div>
                                </li>
                            </xsl:otherwise>
                        </xsl:choose>
                        
                       
                        
                    
                    
                    </xsl:for-each>

                    
                        
                   
                </ol>
            </body>

        </html>
    </xsl:template>

</xsl:stylesheet>
