#!/bin/bash

# set -e : Exit immediately if a command exits with a non-zero status.
set -e

# set -u : Treat unset parameters as an error when substituting.
set -u

# set -f : Avoid globbing / expansion of *
set -f

function create_user_and_database() {
  local d=$1
  local u=$2
  local p=$3
  echo "Creating db $d for user $u with password $p"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER $u WITH PASSWORD '$p';
    CREATE DATABASE "$d";
    GRANT ALL PRIVILEGES ON DATABASE "$d" TO $u;
    \c "$d";
    CREATE EXTENSION IF NOT EXISTS pgcrypto;
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ] && [ -n "$POSTGRES_MULTIPLE_USERS" ] && [ -n "$POSTGRES_MULTIPLE_PASSWORDS" ]; then
  databases=(${POSTGRES_MULTIPLE_DATABASES//,/ })
  users=(${POSTGRES_MULTIPLE_USERS//,/ })
  passwords=(${POSTGRES_MULTIPLE_PASSWORDS//,/ })
  echo "Multiple database creation requested with:"
  printf '  databases: '; printf '%s ' "${databases[@]}"; echo
  printf '  users    : '; printf '%s ' "${users[@]}"; echo
  printf '  passwords: '; printf '%s ' "${passwords[@]}"; echo

  for ((i=0;i<${#databases[@]};++i)); do
    create_user_and_database ${databases[i]} ${users[i]} ${passwords[i]}
  done
fi
