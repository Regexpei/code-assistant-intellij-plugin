package cn.regexp.code.assistant;

import cn.regexp.code.assistant.factory.CodeAssistantToolWindowFactory;
import cn.regexp.code.assistant.ui.CodeAssistantSettings;
import cn.regexp.code.assistant.ui.CodeAssistantUiModule;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.*;
import com.intellij.openapi.application.ApplicationManager;

/**
 * @author Regexpei
 * @date 2024/7/10 22:15
 * @description CodeAssistantModule
 * @since 1.0.0
 */
public class CodeAssistantModule extends AbstractModule {

    protected CodeAssistantModule() {}

    /**
     * 使用 Guice 给 CodeAssistantModule 对象创建一个注入器，
     * 并使用 Suppliers 进行缓存，确保仅创建一次（单例）
     */
    protected static final Supplier<Injector> INJECTOR = Suppliers.memoize(
            () -> Guice.createInjector(new CodeAssistantModule()));

    public static <T> T getInstance(Class<T> type) {
        return INJECTOR.get().getInstance(type);
    }

    @Override
    protected void configure() {
        setupSettingsProvider();

        bind(CodeAssistantToolWindowFactory.class);

        install(new CodeAssistantUiModule());
    }

    protected void setupSettingsProvider() {
        Provider<CodeAssistantSettings> settingsProvider = () -> ApplicationManager.getApplication()
                .getService(CodeAssistantSettings.class);
        bind(CodeAssistantSettings.class).toProvider(settingsProvider).in(Singleton.class);
    }

}
