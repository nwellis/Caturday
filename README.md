# Caturday

Simple app to demonstrate some typical Android examples.

> Everyday is a Caturday

## Running the app
1. You'll need an api key from the Cat API, which you can get from [here](https://thecatapi.com/).
2. Add it to your `gradle.properties` file (`/Users/<your username>/.gradle/gradle.properties` for mac users)
```
catApiKey="<YOUR KEY HERE>"
```
3. When you build the application gradle will pick up this value automatically.
4. Or you can just install the debug [APK](./app-debug.apk) instead.

## Overview
- Min SDK 19
- Uses (Jetpack) Pagination
- Uses (Jetpack) LiveData and LifecycleObservables
- Uses material design library
- Networking with Retrofit + GSON + coroutines
- Dependency Injection with Dagger2

## Known Issues
1. `W/OkHttpClient: A connection to https://25.media.tumblr.com/ was leaked. Did you forget to close a response body?`

There are a few [leak issues](https://github.com/bumptech/glide/issues?utf8=%E2%9C%93&q=is%3Aissue+is%3Aopen+leak) on Glide's github page. It doesn't happen on every device. I need to look into this. Might have something to do with redirects?

