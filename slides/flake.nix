{
  inputs = {
    nixpkgs.url = github:nixos/nixpkgs/nixos-24.05;
    flake-utils.url = github:numtide/flake-utils;
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        scalaOverlay = f: p: {
          mao = p.scala_3.overrideAttrs (old: rec {
						version = "3.5.0";
            bare = p.scala_3.bare.overrideAttrs (oldbare: rec {
              version = "3.5.0";
							src = builtins.fetchurl {
								url = "https://github.com/scala/scala3/releases/download/${version}/scala3-${version}.tar.gz";
								# sha256 = "sha256:1nlppxj5i7vxbpias6kq1ynymbb4sivg909wvwhp37jmf44yh5x5"; # 3.5.1
								# sha256 = "sha256:05pl4nlrimnyldzwsbgw489h2wz596n04gfsdv97fq23xnqkf9zc"; # 3.4.0
								sha256 = "sha256:0bb13baxg89hwwxq16df0x7z2lxgbb3m8p6pwzd4069zc9wd3jms";
							};
            });
						installPhase = ''
    mkdir -p $out/bin
    ln -s ${bare}/bin/scalac $out/bin/scalac
    ln -s ${bare}/bin/scaladoc $out/bin/scaladoc
    ln -s ${bare}/bin/scala $out/bin/scala
    ln -s ${bare}/bin/common $out/bin/common
  '';
          });
        };
        pkgs = import nixpkgs {
          inherit system;
          overlays = [scalaOverlay];
        };
      in
        {
          devShell = pkgs.mkShell {
            name = "fs2-workshop-shell";
            buildInputs = with pkgs; [
              althttpd
              scala-cli
              # mao
							coursier
              # visualvm
            ];
          };
        }
    );
}
