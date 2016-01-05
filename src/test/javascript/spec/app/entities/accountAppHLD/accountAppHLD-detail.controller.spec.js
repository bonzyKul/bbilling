'use strict';

describe('AccountAppHLD Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAccountAppHLD, MockAccounts, MockProducts;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAccountAppHLD = jasmine.createSpy('MockAccountAppHLD');
        MockAccounts = jasmine.createSpy('MockAccounts');
        MockProducts = jasmine.createSpy('MockProducts');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AccountAppHLD': MockAccountAppHLD,
            'Accounts': MockAccounts,
            'Products': MockProducts
        };
        createController = function() {
            $injector.get('$controller')("AccountAppHLDDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:accountAppHLDUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
