package org.exoplatform.test.matchers;


import org.exoplatform.commons.api.settings.SettingValue;
import org.mockito.ArgumentMatcher;

public class SettingValueMatcher implements ArgumentMatcher<SettingValue<?>> {

    private SettingValue<?> expected;

    public SettingValueMatcher(SettingValue<?> expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(SettingValue<?> actual) {
        return expected.getValue().equals(actual.getValue());
    }
}