package me.filizes.loggercommand.logging;

import com.google.gson.JsonObject;
import me.filizes.loggercommand.config.ConfigManager;
import okhttp3.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WebhookSender {

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();

    public static void sendLog(@NotNull Plugin plugin, @NotNull String command, @NotNull String arguments, @NotNull String playerName, @NotNull String timestamp) {
        ConfigManager config = new ConfigManager(plugin);
        List<String> webhookUrls = config.getWebhookUrls();

        if (webhookUrls.isEmpty()) {
            plugin.getLogger().warning("[LoggerCommand] Webhook URL не указан в config.yml!");
            return;
        }

        String message = config.getWebhookMessage()
                .replace("{command}", command)
                .replace("{arguments}", arguments.isEmpty() ? "(без аргументов)" : arguments)
                .replace("{player}", playerName)
                .replace("{time}", timestamp);

        JsonObject jsonPayload = new JsonObject();
        jsonPayload.addProperty("content", message);

        webhookUrls.forEach(url -> CompletableFuture.runAsync(() -> sendRequest(plugin, url, jsonPayload.toString())));
    }

    private static void sendRequest(@NotNull Plugin plugin, @NotNull String url, @NotNull String jsonPayload) {
        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "LoggerCommand-Bot")
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (!response.isSuccessful()) {
                    plugin.getLogger().warning("[LoggerCommand] Ошибка Webhook: " + response.code() + " - " + response.message());
                }
                response.close();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                plugin.getLogger().severe("[LoggerCommand] Ошибка отправки Webhook: " + e.getMessage());
            }
        });
    }
}