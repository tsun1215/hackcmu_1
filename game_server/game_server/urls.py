from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'game_server.views.home', name='home'),
    # url(r'^game_server/', include('game_server.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
    url(r'^$', 'game_server.views.test'),
    url(r'^register/', 'game_server.views.register_device'),
    url(r'^game/new/', 'game_server.views.create_game'),
    url(r'^game/refresh/', 'game_server.views.get_bombs'),
    url(r'^game/join/', 'game_server.views.join_game'),
    url(r'^game/list/', 'game_server.views.list_games'),
    url(r'^game/bomb/', 'game_server.views.place_bomb'),
    url(r'^game/explode/', 'game_server.views.bomb_player'),
)
