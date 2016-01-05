'use strict';

describe('AccountPreferences Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAccountPreferences, MockAccounts;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAccountPreferences = jasmine.createSpy('MockAccountPreferences');
        MockAccounts = jasmine.createSpy('MockAccounts');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AccountPreferences': MockAccountPreferences,
            'Accounts': MockAccounts
        };
        createController = function() {
            $injector.get('$controller')("AccountPreferencesDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:accountPreferencesUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
