package conversion.types.enums;

import lombok.Getter;

public enum PostgreSQLTypes {
    SERIAL("serial", "serial"),
    XML("xml", "xml"),
    BIGINT("bigint", "bigint"),
    BIT("bit", "bit"),
    VARBIT("bit varying", "varbit"),
    BOOLEAN("boolean", "bool"),
    BOX("box", "box"),
    BYTEA("bytea", "bytea"),
    TXID("txid_snapshot", "txid"),
    UUID("uuid", "uuid"),
    CIDR("cidr", "cidr"),
    CIRCLE("circle", "circle"),
    DATE("date", "date"),
    FLOAT8("double precision", "double"),
    INET("inet", "inet"),
    INT("integer", "int"),
    INTERVAL("interval", "interval"),
    LINE("line", "line"),
    LSEG("lseg", "lseg"),
    MACADDR("macaddr", "macaddr"),
    MONEY("money", "money"),
    NUMERIC("numeric", "num"),
    PATH("path", "path"),
    POINT("point", "point"),
    POLYGON("polygon", "polygon"),
    REAL("real", "real"),
    SMALLINT("smallint", "smallint"),
    TIME("time without time zone", "time"),
    TIMETZ("time with time zone", "timetz"),
    TIMESTAMP("timestamp without time zone", "timestamp"),
    TIMESTAMPTZ("timestamp with time zone", "timestamptz"),
    TSQUERY("tsquery", "tsquery"),
    TSVECTOR("tsvector", "tsvector"),
    VARCHAR("character varying", "varchar"),
    CHAR("character", "char"),
    TEXT("text", "text");

    @Getter
    private final String name;

    @Getter
    private final String mark;

    PostgreSQLTypes(String name, String mark) {
        this.name = name;
        this.mark = mark;
    }
}
