import exchange.gbc.S2stocksApplication
welcome = { ->
    def hostName;
    try {
        hostName = java.net.InetAddress.getLocalHost().getHostName();
    } catch (java.net.UnknownHostException ignore) {
        hostName = 'localhost';
    }

    String banner = S2stocksApplication.getResourceAsStream('/banner.txt').text

    return """
${banner}
Logged into $hostName @ ${new Date()}
"""
}

prompt = { ->
    return "% ";
}