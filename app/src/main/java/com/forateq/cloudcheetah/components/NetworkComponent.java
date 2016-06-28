package com.forateq.cloudcheetah.components;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.authenticate.AuthenticatorActivity;
import com.forateq.cloudcheetah.modules.AppModule;
import com.forateq.cloudcheetah.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 * Created by Vallejos Family on 5/12/2016.
 */
@Singleton
@Component(modules={AppModule.class, NetworkModule.class})
public interface NetworkComponent {

    void inject(MainActivity activity);
    void inject(AuthenticatorActivity activity);
}
