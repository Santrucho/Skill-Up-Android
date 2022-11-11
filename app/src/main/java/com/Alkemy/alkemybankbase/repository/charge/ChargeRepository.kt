package com.Alkemy.alkemybankbase.repository.charge

interface ChargeRepository {
    /***********************************************************
        JUST WRITE EMPTY FUNCTIONS WITH NO REAL IMPLEMENTATIONS
     ************************************************************/

    /*Unit Tests should not have external dependencies.
    In our case we have use injections like this ViewModel <- Repository <- ApiService <- RetrofitInstance
    Which means that to write a test for a form validation function from the viewmodel,
    you need to instantiate a viewmodel with a repository that contains all the rest.
    Usually you can use a "mock" for that, but since at the end of the chain there are some
    @Provides from the AppModule, a mock class would not work. The solution would be to make
    an Instrumented Unit test which isn't what the ticket asked to do.
    As an alternative, the ViewModel will have an Interface on the constructor, if used on the
    activities the ViewModel will have an implemented version received from the AppModule and when
    used on a test they will use a fake version that doesn't have that chain of dependencies.
     */
}