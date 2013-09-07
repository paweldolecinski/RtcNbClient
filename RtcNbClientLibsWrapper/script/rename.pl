#!/usr/local/bin/perl
#
# Usage: rename perlexpr [files]

#$regexp = 's/-.+\.jar$/_3.0iFix1.jar/';
$regexp = 's/_/-/';
#|| die "Usage:  rename perlexpr [filenames]\n";

if (!@ARGV) {
   @ARGV = <STDIN>;
   chomp(@ARGV);
}


foreach $_ (@ARGV) {
   $old_name = $_;
   eval $regexp;
   die $@ if $@;
   rename($old_name, $_) unless $old_name eq $_;
}

exit(0);