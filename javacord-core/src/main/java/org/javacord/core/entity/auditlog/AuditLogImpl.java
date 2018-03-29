package org.javacord.core.entity.auditlog;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.auditlog.AuditLog;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.webhook.WebhookImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The implementation of {@link AuditLog}.
 */
public class AuditLogImpl implements AuditLog {

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * A collection with all involved webhooks.
     */
    private final Collection<Webhook> involvedWebhooks = new ArrayList<>();

    /**
     * A collection with all involved users.
     */
    private final Collection<User> involvedUsers = new ArrayList<>();

    /**
     * A list with all entries.
     */
    private final List<AuditLogEntry> entries = new ArrayList<>();

    /**
     * Creates a new audit log.
     *
     * @param api The discord api instance.
     * @param data The data of the audit log.
     */
    public AuditLogImpl(DiscordApi api, JsonNode data) {
        this.api = api;
        for (JsonNode webhook : data.get("webhooks")) {
            involvedWebhooks.add(new WebhookImpl(api, webhook));
        }
        for (JsonNode user : data.get("users")) {
            involvedUsers.add(((DiscordApiImpl) api).getOrCreateUser(user));
        }
        for (JsonNode entry : data.get("audit_log_entries")) {
            entries.add(new AuditLogEntryImpl(this, entry));
        }
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public Collection<Webhook> getInvolvedWebhooks() {
        return Collections.unmodifiableCollection(involvedWebhooks);
    }

    @Override
    public Collection<User> getInvolvedUsers() {
        return Collections.unmodifiableCollection(involvedUsers);
    }

    @Override
    public List<AuditLogEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}