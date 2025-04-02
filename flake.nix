{
  inputs = {
    nixpkgs.url = github:nixos/nixpkgs/nixos-24.11;
    flake-utils.url = github:numtide/flake-utils;
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
        };
      in
        {
          devShell = pkgs.mkShell {
            name = "fs2-workshop-shell";
            buildInputs = with pkgs; [
              scala-cli
              visualvm
            ];
          };
        }
    );
}
