// Copyright (c) 2020 The DAML Authors. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
import { Optional, Text } from './index';

describe('daml-json-types', () => {
  it('optional', () => {
    const dict = Optional(Text);
    expect(dict.decoder().run(null).ok).toBe(true);
    expect(dict.decoder().run('X').ok).toBe(true);
    expect(dict.decoder().run([]).ok).toBe(false);
    expect(dict.decoder().run(['X']).ok).toBe(false);
  });

  it('nested optionals', () => {
    const dict = Optional(Optional(Text));
    expect(dict.decoder().run(null).ok).toBe(true);
    expect(dict.decoder().run([]).ok).toBe(true);
    expect(dict.decoder().run(['X']).ok).toBe(true);
    expect(dict.decoder().run('X').ok).toBe(false);
    expect(dict.decoder().run([['X']]).ok).toBe(false);
    expect(dict.decoder().run([[]]).ok).toBe(false);
    // FIXME(MH): The decoder for `Optional` is slightly off in this case.
    // expect(dict.decoder().run([null]).ok).toBe(false);
  });
});
