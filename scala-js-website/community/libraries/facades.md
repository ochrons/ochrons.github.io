---
layout: community
title: JavaScript library facades
---

## JavaScript libraries for Scala.js

These facades wrap existing JavaScript libraries, giving you type safe access to their functionality. Some of the
facades may only partially expose the underlying JavaScript library functionality, so make sure to check out the
details. To quickly start using one of these libraries, just click on the dependency clipboard button to get the
relevant SBT dependency definition.

{% include library.html lib=site.data.library.jsfacades %}

If you didn't find a facade for the library you'd like to use, it's quite easy to do one yourself. Check out the
[facade documentation](../../doc/interoperability/calling_js.html) and the
[TypeScript conversion tool](https://github.com/sjrd/scala-js-ts-importer).

-------

<small markdown="1">Additions and corrections to this section may be reported through
[GitHub issues](https://github.com/scala-js/scala-js-website/issues). Please include Name, Url, Description and
Dependency</small>
