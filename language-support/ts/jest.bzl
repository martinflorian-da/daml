# Copyright (c) 2020 The DAML Authors. All rights reserved.
# SPDX-License-Identifier: Apache-2.0

load("@language_support_ts_deps//jest-cli:index.bzl", _jest_test = "jest_test")

def jest_test(name, srcs, deps, jest_config = ":jest.config.js", tsconfig = ":tsconfig.json", **kwargs):
    "Macro for TypeScript test suites with jest"
    args = [
        "--no-cache",
        "--no-watchman",
        "--ci",
    ]
    args.extend(["--config", "$(location %s)" % jest_config])
    for src in srcs:
        args.extend(["--runTestsByPath", "$(location %s)" % src])
    jest_deps = [
        "@language_support_ts_deps//@types/jest",
        "@language_support_ts_deps//jest",
        "@language_support_ts_deps//ts-jest",
        "@language_support_ts_deps//typescript",
    ]

    _jest_test(
        name = name,
        data = [jest_config, tsconfig] + srcs + deps + jest_deps,
        args = args,
        **kwargs
    )
