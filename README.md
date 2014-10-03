Trio Shows
==========

Trio shows concert search app

Notable tech:
 - Uses Google's open source Volley library to do HTTP requests and request caching: http://developer.android.com/training/volley/index.html
 - Customized com.android.volley.toolbox.NetworkImageView --> FadingNetworkImageView that fades in when image loads: https://github.com/gsajith/Trio/blob/master/TrioApp/app/src/main/java/com/volley/adapter/FadingNetworkImageView.java
 - Investigating placeholder prefetching a-la the Pinterest app: http://blog.embed.ly/post/51071740487/pinterest-colored-background-placeholders
