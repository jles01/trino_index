/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.metadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.trino.connector.CatalogName;
import io.trino.spi.connector.ConnectorTableHandle;
import io.trino.spi.connector.ConnectorTableLayoutHandle;
import io.trino.spi.connector.ConnectorTransactionHandle;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class TableHandle
{
    private final CatalogName catalogName;
    private final ConnectorTableHandle connectorHandle;
    private final ConnectorTransactionHandle transaction;

    // Table layouts are deprecated, but we keep this here to hide the notion of layouts
    // from the engine. TODO: it should be removed once table layouts are finally deleted
    private final Optional<ConnectorTableLayoutHandle> layout;

    @JsonCreator
    public TableHandle(
            @JsonProperty("catalogName") CatalogName catalogName,
            @JsonProperty("connectorHandle") ConnectorTableHandle connectorHandle,
            @JsonProperty("transaction") ConnectorTransactionHandle transaction,
            @JsonProperty("layout") Optional<ConnectorTableLayoutHandle> layout)
    {
        this.catalogName = requireNonNull(catalogName, "catalogName is null");
        this.connectorHandle = requireNonNull(connectorHandle, "connectorHandle is null");
        this.transaction = requireNonNull(transaction, "transaction is null");
        this.layout = requireNonNull(layout, "layout is null");
    }

    @JsonProperty
    public CatalogName getCatalogName()
    {
        return catalogName;
    }

    @JsonProperty
    public ConnectorTableHandle getConnectorHandle()
    {
        return connectorHandle;
    }

    @JsonProperty
    public Optional<ConnectorTableLayoutHandle> getLayout()
    {
        return layout;
    }

    @JsonProperty
    public ConnectorTransactionHandle getTransaction()
    {
        return transaction;
    }

    public TableHandle withConnectorHandle(ConnectorTableHandle connectorHandle)
    {
        return new TableHandle(
                catalogName,
                connectorHandle,
                transaction,
                layout);
    }

    @Override
    public String toString()
    {
        return catalogName + ":" + connectorHandle;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableHandle other = (TableHandle) o;
        return Objects.equals(catalogName, other.catalogName) &&
                Objects.equals(connectorHandle, other.connectorHandle) &&
                Objects.equals(transaction, other.transaction) &&
                Objects.equals(layout, other.layout);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(catalogName, connectorHandle, transaction, layout);
    }
}
