export const navigation = [
    {
        'id'      : 'applications',
        'title'   : 'Applications',
        'translate': 'NAV.APPLICATIONS',
        'type'    : 'group',
        'icon'     : 'ui',
        'children': [
            {
                'id'   : 'dashboard',
                'title': 'Dashboard',
                'translate': 'NAV.DASHBOARD.TITLE',
                'type' : 'item',
                'icon' : 'dashboard',
                'url'  : '/ui/dashboard'
            },
            {
                'id'   : 'user-administration',
                'title': 'User Administration',
                'translate': 'NAV.USER_ADMINISTRATION.TITLE',
                'type' : 'item',
                'icon' : 'accessibility',
                'url'  : '/ui/user-administration'
            },
            {
                'id'   : 'myuser-administration',
                'title': 'myUser Administration',
                'translate': 'NAV.MYUSER_ADMINISTRATION.TITLE',
                'type' : 'item',
                'icon' : 'accessibility',
                'url'  : '/ui/myuser-administration'
            },
            {
                'id'   : 'nomenclatures',
                'title': 'Nomenclatures',
                'translate': 'NAV.NOMENCLATURES.TITLE',
                'type' : 'item',
                'icon' : 'view_module',
                'url'  : '/ui/nomenclatures'
            }

        ]
    }
];
